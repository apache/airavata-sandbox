// Package fileproto provides the gRPC stream client for request/response file operations.
package fileproto

import (
	"context"
	"sync"

	pb "github.com/you/remotefs/proto/gen/remotefs"
	"google.golang.org/grpc"
)

// Stream is the interface for sending and receiving FileMessage (used by both source and sink).
type Stream interface {
	Send(*pb.FileMessage) error
	Recv() (*pb.FileMessage, error)
}

// Client wraps a stream and provides request/response matching by request_id (for the remote/sink side).
type Client struct {
	stream   Stream
	mu       sync.Mutex
	pending  map[uint64]chan *pb.FileResponse
	nextID   uint64
	closed   bool
	recvDone chan struct{}
}

// NewClient creates a client that uses the given stream. Call Run() in a goroutine to dispatch responses.
func NewClient(stream Stream) *Client {
	c := &Client{
		stream:   stream,
		pending:  make(map[uint64]chan *pb.FileResponse),
		recvDone: make(chan struct{}),
	}
	return c
}

// Run receives messages from the stream and dispatches responses to waiters. Call once in a goroutine.
// Returns when Recv fails or stream is closed.
func (c *Client) Run() {
	for {
		msg, err := c.stream.Recv()
		if err != nil {
			break
		}
		resp := msg.GetResponse()
		if resp == nil {
			continue
		}
		c.mu.Lock()
		ch := c.pending[resp.RequestId]
		delete(c.pending, resp.RequestId)
		c.mu.Unlock()
		if ch != nil {
			select {
			case ch <- resp:
			default:
				// waiter gave up
			}
		}
	}
	c.mu.Lock()
	c.closed = true
	for _, ch := range c.pending {
		close(ch)
	}
	c.pending = make(map[uint64]chan *pb.FileResponse)
	c.mu.Unlock()
	close(c.recvDone)
}

// Do sends the request and blocks until the response with the same request_id is received.
func (c *Client) Do(ctx context.Context, req *pb.FileRequest) (*pb.FileResponse, error) {
	c.mu.Lock()
	if c.closed {
		c.mu.Unlock()
		return nil, grpc.ErrClientConnClosing
	}
	c.nextID++
	req.RequestId = c.nextID
	reqID := req.RequestId
	ch := make(chan *pb.FileResponse, 1)
	c.pending[reqID] = ch
	c.mu.Unlock()

	defer func() {
		c.mu.Lock()
		delete(c.pending, reqID)
		c.mu.Unlock()
	}()

	msg := &pb.FileMessage{Payload: &pb.FileMessage_Request{Request: req}}
	if err := c.stream.Send(msg); err != nil {
		return nil, err
	}

	select {
	case resp := <-ch:
		if resp == nil {
			return nil, grpc.ErrClientConnClosing
		}
		return resp, nil
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-c.recvDone:
		return nil, grpc.ErrClientConnClosing
	}
}

// Close marks the client closed and unblocks all waiters.
func (c *Client) Close() {
	c.mu.Lock()
	c.closed = true
	for _, ch := range c.pending {
		close(ch)
	}
	c.pending = make(map[uint64]chan *pb.FileResponse)
	c.mu.Unlock()
}

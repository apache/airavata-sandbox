package source

import (
	"context"
	"io"
	"log"
	"net"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"

	"github.com/you/remotefs/internal/export"
	pb "github.com/you/remotefs/proto/gen/remotefs"
)

// Server is an in-process gRPC server that serves a single export (one backend).
// It implements only ConnectSink; RegisterExport and ConnectSource return Unimplemented.
type Server struct {
	pb.UnimplementedRemotefsCoordinatorServer
	backend *export.Backend
}

// NewServer creates a server that will serve file operations from backend.
func NewServer(backend *export.Backend) *Server {
	return &Server{backend: backend}
}

// RegisterExport returns Unimplemented (this server has a single fixed export).
func (s *Server) RegisterExport(context.Context, *pb.RegisterExportRequest) (*pb.RegisterExportResponse, error) {
	return nil, status.Error(codes.Unimplemented, "method RegisterExport not implemented")
}

// ConnectSource returns Unimplemented (sources connect in-process via the single backend).
func (s *Server) ConnectSource(grpc.BidiStreamingServer[pb.FileMessage, pb.FileMessage]) error {
	return status.Error(codes.Unimplemented, "method ConnectSource not implemented")
}

// ConnectSink accepts a mount client: first message must be ConnectSinkRequest (session_id ignored),
// then request/response loop using the backend.
func (s *Server) ConnectSink(stream grpc.BidiStreamingServer[pb.FileMessage, pb.FileMessage]) error {
	msg, err := stream.Recv()
	if err != nil {
		return err
	}
	if msg.GetConnectSink() == nil {
		return status.Error(codes.InvalidArgument, "first message must be ConnectSinkRequest")
	}

	ctx := stream.Context()
	for {
		msg, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			log.Printf("ConnectSink recv error: %v", err)
			return err
		}
		req := msg.GetRequest()
		if req == nil {
			continue
		}
		resp := s.backend.HandleRequest(ctx, req)
		if err := stream.Send(&pb.FileMessage{Payload: &pb.FileMessage_Response{Response: resp}}); err != nil {
			log.Printf("ConnectSink send error: %v", err)
			return err
		}
	}
}

// Run starts the gRPC server on the given listener.
func Run(ctx context.Context, lis net.Listener, srv *Server) error {
	grpcServer := grpc.NewServer()
	pb.RegisterRemotefsCoordinatorServer(grpcServer, srv)
	return grpcServer.Serve(lis)
}

package source

import (
	"context"
	"net"
	"os"
	"path/filepath"
	"testing"
	"time"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"

	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/export"
	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/fileproto"
	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

func TestSourceServerConnectSinkAndRead(t *testing.T) {
	ctx := context.Background()
	dir := t.TempDir()
	if err := os.WriteFile(filepath.Join(dir, "hello.txt"), []byte("hello"), 0644); err != nil {
		t.Fatal(err)
	}
	virtualName := "data"
	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: virtualName}}
	backend := export.NewBackend(paths)
	srv := NewServer(backend)

	lis, err := net.Listen("tcp", "127.0.0.1:0")
	if err != nil {
		t.Fatal(err)
	}
	defer lis.Close()
	grpcServer := grpc.NewServer()
	pb.RegisterRemotefsCoordinatorServer(grpcServer, srv)
	go grpcServer.Serve(lis)
	defer grpcServer.GracefulStop()

	conn, err := grpc.NewClient(lis.Addr().String(), grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		t.Fatal(err)
	}
	defer conn.Close()
	client := pb.NewRemotefsCoordinatorClient(conn)

	stream, err := client.ConnectSink(ctx)
	if err != nil {
		t.Fatal(err)
	}
	err = stream.Send(&pb.FileMessage{
		Payload: &pb.FileMessage_ConnectSink{ConnectSink: &pb.ConnectSinkRequest{}},
	})
	if err != nil {
		t.Fatal(err)
	}
	fpClient := fileproto.NewClient(stream)
	go fpClient.Run()

	time.Sleep(50 * time.Millisecond)

	// Lookup root -> "data"
	lookupResp, err := fpClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Lookup{Lookup: &pb.LookupRequest{Path: "", Name: virtualName}},
	})
	if err != nil {
		t.Fatal(err)
	}
	if lookupResp.Errno != 0 {
		t.Fatalf("lookup root/data: errno=%d", lookupResp.Errno)
	}
	lookupResp2, err := fpClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Lookup{Lookup: &pb.LookupRequest{Path: virtualName, Name: "hello.txt"}},
	})
	if err != nil {
		t.Fatal(err)
	}
	if lookupResp2.Errno != 0 {
		t.Fatalf("lookup data/hello.txt: errno=%d", lookupResp2.Errno)
	}
	openResp, err := fpClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Open{Open: &pb.OpenRequest{Path: virtualName + "/hello.txt", Flags: 0}},
	})
	if err != nil {
		t.Fatal(err)
	}
	if openResp.Errno != 0 {
		t.Fatalf("open: errno=%d", openResp.Errno)
	}
	handleID := openResp.GetOpen().HandleId
	readResp, err := fpClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
			Path: virtualName + "/hello.txt", HandleId: handleID, Offset: 0, Size: 10,
		}},
	})
	if err != nil {
		t.Fatal(err)
	}
	if readResp.Errno != 0 {
		t.Fatalf("read: errno=%d", readResp.Errno)
	}
	data := readResp.GetRead().Data
	if string(data) != "hello" {
		t.Fatalf("read content: got %q, want %q", data, "hello")
	}
}

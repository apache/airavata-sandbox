//go:build linux

package commands

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/hanwen/go-fuse/v2/fs"
	"github.com/hanwen/go-fuse/v2/fuse"
	"github.com/spf13/cobra"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"

	"github.com/you/remotefs/internal/fileproto"
	"github.com/you/remotefs/internal/mount"
	"github.com/you/remotefs/internal/resolver"
	pb "github.com/you/remotefs/proto/gen/remotefs"
)

var (
	mountPoint  string
	mountServer string
)

func init() {
	mountCmd = &cobra.Command{
		Use:   "mount",
		Short: "Mount published folder at mountpoint (point -s at tunneled publish server)",
		RunE:  runMount,
	}
	mountCmd.Flags().StringVarP(&mountPoint, "mountpoint", "m", "", "Mount point directory")
	mountCmd.Flags().StringVarP(&mountServer, "server", "s", "", "Server address (tunneled publish endpoint, e.g. localhost:50051)")
	_ = mountCmd.MarkFlagRequired("mountpoint")
	_ = mountCmd.MarkFlagRequired("server")
}

func runMount(cmd *cobra.Command, args []string) error {
	if _, err := os.Stat(mountPoint); err != nil {
		return fmt.Errorf("mountpoint %q: %w", mountPoint, err)
	}
	serverAddr, err := resolver.ResolveServer(mountServer)
	if err != nil {
		return err
	}
	conn, err := grpc.NewClient(serverAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return err
	}
	defer conn.Close()
	client := pb.NewRemotefsCoordinatorClient(conn)
	stream, err := client.ConnectSink(context.Background())
	if err != nil {
		return err
	}
	err = stream.Send(&pb.FileMessage{
		Payload: &pb.FileMessage_ConnectSink{ConnectSink: &pb.ConnectSinkRequest{}},
	})
	if err != nil {
		return err
	}
	fpClient := fileproto.NewClient(stream)
	go fpClient.Run()
	root := &mount.RemoteRoot{Client: fpClient}
	sec := time.Second
	opts := &fs.Options{
		AttrTimeout:  &sec,
		EntryTimeout: &sec,
		MountOptions: fuse.MountOptions{AllowOther: true},
	}
	server, err := fs.Mount(mountPoint, root, opts)
	if err != nil {
		return err
	}
	go func() {
		sig := make(chan os.Signal, 1)
		signal.Notify(sig, syscall.SIGINT, syscall.SIGTERM)
		<-sig
		_ = server.Unmount()
		fpClient.Close()
	}()
	log.Printf("Mounted at %s", mountPoint)
	server.Wait()
	return nil
}

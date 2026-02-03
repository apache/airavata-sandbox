//go:build linux

package commands

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/hanwen/go-fuse/v2/fs"
	"github.com/hanwen/go-fuse/v2/fuse"
	"github.com/spf13/cobra"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"

	"github.com/you/remotefs/internal/fileproto"
	"github.com/you/remotefs/internal/frpclient"
	"github.com/you/remotefs/internal/mount"
	"github.com/you/remotefs/internal/resolver"
	pb "github.com/you/remotefs/proto/gen/remotefs"
)

var (
	mountPoint   string
	mountServer  string
	mountID      string
	mountSecret  string
	mountFRPSrv  string
	mountFRPToken string
	allowOther   bool
)

func init() {
	mountCmd = &cobra.Command{
		Use:   "mount",
		Short: "Mount published folder at mountpoint (point -s at tunneled publish server or use --id for FRP)",
		RunE:  runMount,
	}
	mountCmd.Flags().StringVarP(&mountPoint, "mountpoint", "m", "", "Mount point directory")
	mountCmd.Flags().StringVarP(&mountServer, "server", "s", "", "Server address (tunneled publish endpoint, e.g. localhost:50051)")
	mountCmd.Flags().StringVar(&mountID, "id", "", "Forwarding ID from publish --frp (or id:secret for both)")
	mountCmd.Flags().StringVar(&mountSecret, "secret", "", "Secret from publish --frp (omit if using id:secret in --id)")
	mountCmd.Flags().StringVar(&mountFRPSrv, "frp-server", "", "FRP server address when using --id. Env: REMOTEFS_FRP_SERVER")
	mountCmd.Flags().StringVar(&mountFRPToken, "frp-token", "", "FRP server auth token when using --id. Env: REMOTEFS_FRP_TOKEN")
	mountCmd.Flags().BoolVar(&allowOther, "allow-other", false, "allow other users to access the mount (requires user_allow_other in /etc/fuse.conf)")
	_ = mountCmd.MarkFlagRequired("mountpoint")
}

func runMount(cmd *cobra.Command, args []string) error {
	if _, err := os.Stat(mountPoint); err != nil {
		return fmt.Errorf("mountpoint %q: %w", mountPoint, err)
	}

	// Resolve server address: either -s or --id (FRP visitor)
	var serverAddr string
	var frpCancel func()
	if mountServer != "" && mountID != "" {
		return fmt.Errorf("use either --server (-s) or --id, not both")
	}
	if mountServer != "" {
		var err error
		serverAddr, err = resolver.ResolveServer(mountServer)
		if err != nil {
			return err
		}
	} else if mountID != "" {
		id := mountID
		secret := mountSecret
		if strings.Contains(id, ":") {
			parts := strings.SplitN(id, ":", 2)
			id, secret = parts[0], parts[1]
		}
		if secret == "" {
			return fmt.Errorf("--secret required when using --id (or use --id id:secret)")
		}
		server, token := frpclient.FRPServerAndToken(mountFRPSrv, mountFRPToken)
		if err := frpclient.CheckFRPServerReachable(server, 0); err != nil {
			return err
		}
		common, err := frpclient.CommonConfig(server, token)
		if err != nil {
			return fmt.Errorf("frp config: %w", err)
		}
		serverAddr, frpCancel, err = frpclient.RunMountVisitors(context.Background(), common, id, secret)
		if err != nil {
			return fmt.Errorf("frp visitor: %w", err)
		}
		defer frpCancel()
	} else {
		return fmt.Errorf("either --server (-s) or --id with --secret is required")
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
		MountOptions: fuse.MountOptions{AllowOther: allowOther},
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

//go:build linux

package commands

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
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
	mountAddr   string
	mountToken  string
	mountFRP    string
	allowOther  bool
)

func init() {
	mountCmd = &cobra.Command{
		Use:   "mount <mountpoint>",
		Short: "Mount published folder at mountpoint (use --addr for direct gRPC or --token for FRP)",
		RunE:  runMount,
		Args:  cobra.ExactArgs(1),
	}
	mountCmd.Flags().StringVarP(&mountAddr, "addr", "a", "", "gRPC server address (e.g. localhost:50051)")
	mountCmd.Flags().StringVar(&mountToken, "token", "", "Forwarding token from publish --frp (id:secret)")
	mountCmd.Flags().StringVar(&mountFRP, "frp", "", "FRP connection when using --token: hostname:port:password. Env: REMOTEFS_FRP")
	mountCmd.Flags().BoolVar(&allowOther, "allow-other", false, "allow other users to access the mount (requires user_allow_other in /etc/fuse.conf)")
}

func runMount(cmd *cobra.Command, args []string) error {
	mp := args[0]
	if _, err := os.Stat(mp); err != nil {
		return fmt.Errorf("mountpoint %q: %w", mp, err)
	}

	var serverAddr string
	var frpCancel func()
	if mountAddr != "" && mountToken != "" {
		return fmt.Errorf("use either --addr (-a) or --token, not both")
	}
	if mountAddr != "" {
		var err error
		serverAddr, err = resolver.ResolveServer(mountAddr)
		if err != nil {
			return err
		}
	} else if mountToken != "" {
		parts := strings.SplitN(mountToken, ":", 2)
		if len(parts) != 2 || parts[0] == "" || parts[1] == "" {
			return fmt.Errorf("--token must be id:secret (from publish --frp output)")
		}
		id, secret := parts[0], parts[1]
		server, authToken, err := frpclient.FRPConnection(mountFRP)
		if err != nil {
			return fmt.Errorf("frp connection: %w", err)
		}
		if err := frpclient.CheckFRPServerReachable(server, 0); err != nil {
			return err
		}
		common, err := frpclient.CommonConfig(server, authToken)
		if err != nil {
			return fmt.Errorf("frp config: %w", err)
		}
		serverAddr, frpCancel, err = frpclient.RunMountVisitors(context.Background(), common, id, secret)
		if err != nil {
			return fmt.Errorf("frp visitor: %w", err)
		}
		defer frpCancel()
	} else {
		return fmt.Errorf("either --addr (-a) or --token is required")
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
	server, err := fs.Mount(mp, root, opts)
	if err != nil {
		return err
	}
	done := make(chan struct{})
	go func() {
		sig := make(chan os.Signal, 1)
		signal.Notify(sig, syscall.SIGINT, syscall.SIGTERM)
		<-sig
		signal.Stop(sig)
		log.Printf("Unmounting %s...", mp)
		// Close client first so in-flight FUSE ops fail and unmount can complete.
		fpClient.Close()
		_ = conn.Close()
		go func() {
			_ = server.Unmount()
			close(done)
		}()
		unmountTimeout := 10 * time.Second
		select {
		case <-done:
			// Unmount finished
		case <-time.After(unmountTimeout):
			log.Printf("Unmount timed out after %v", unmountTimeout)
		}
		// Always run fusermount -u so the kernel mount is cleared and the directory
		// is not left as d????????? / "Transport endpoint is not connected".
		var forceErr error
		for _, name := range []string{"fusermount", "fusermount3"} {
			forceErr = exec.Command(name, "-u", mp).Run()
			if forceErr == nil {
				break
			}
		}
		if forceErr != nil {
			log.Printf("Unmount failed: %v; run manually: fusermount -u %q", forceErr, mp)
		}
		os.Exit(0)
	}()
	log.Printf("Mounted at %s", mp)
	server.Wait()
	return nil
}

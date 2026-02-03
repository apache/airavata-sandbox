package commands

import (
	"context"
	"fmt"
	"net"
	"os"
	"path/filepath"

	"github.com/spf13/cobra"

	"github.com/you/remotefs/internal/export"
	"github.com/you/remotefs/internal/frpclient"
	"github.com/you/remotefs/internal/source"
	pb "github.com/you/remotefs/proto/gen/remotefs"
)

var (
	publishFolder  string
	publishAddr    string
	publishFRP     bool
	publishFRPSrv  string
	publishFRPToken string
)

var publishCmd = &cobra.Command{
	Use:   "publish [folder]",
	Short: "Publish a folder: start gRPC server in-process and serve it (tunnel the port to remote for mount)",
	RunE:  runPublish,
}

func init() {
	publishCmd.Flags().StringVarP(&publishFolder, "folder", "f", "", "Folder path to publish")
	publishCmd.Flags().StringVarP(&publishAddr, "addr", "a", ":50051", "Listen address (e.g. :50051)")
	publishCmd.Flags().BoolVar(&publishFRP, "frp", false, "Register with FRP server and print forwarding ID and secret for remote mount")
	publishCmd.Flags().StringVar(&publishFRPSrv, "frp-server", "", "FRP server address (host or host:port). Env: REMOTEFS_FRP_SERVER")
	publishCmd.Flags().StringVar(&publishFRPToken, "frp-token", "", "FRP server auth token. Env: REMOTEFS_FRP_TOKEN")
}

func runPublish(cmd *cobra.Command, args []string) error {
	folder := publishFolder
	if folder == "" && len(args) > 0 {
		folder = args[0]
	}
	if folder == "" {
		return cmd.Usage()
	}
	folder = filepath.Clean(folder)
	info, err := os.Stat(folder)
	if err != nil {
		return err
	}
	if !info.IsDir() {
		return fmt.Errorf("%s: not a directory", folder)
	}
	virtualName := filepath.Base(folder)
	if virtualName == "." || virtualName == "/" {
		virtualName = "data"
	}
	paths := []*pb.ExportPath{
		{LocalPath: folder, VirtualName: virtualName},
	}

	backend := export.NewBackend(paths)
	srv := source.NewServer(backend)

	listenAddr := publishAddr
	if publishFRP {
		listenAddr = "127.0.0.1:0"
	}
	host, portStr, err := net.SplitHostPort(listenAddr)
	if err != nil {
		return fmt.Errorf("invalid addr %q: %w", listenAddr, err)
	}
	if host == "" {
		host = "0.0.0.0"
	}
	addr := net.JoinHostPort(host, portStr)
	lis, err := net.Listen("tcp", addr)
	if err != nil {
		return err
	}
	defer lis.Close()

	var frpCancel func()
	if publishFRP {
		server, token := frpclient.FRPServerAndToken(publishFRPSrv, publishFRPToken)
		if err := frpclient.CheckFRPServerReachable(server, 0); err != nil {
			return err
		}
		id, secret, err := frpclient.GenerateIDSecret()
		if err != nil {
			return fmt.Errorf("generate id/secret: %w", err)
		}
		common, err := frpclient.CommonConfig(server, token)
		if err != nil {
			return fmt.Errorf("frp config: %w", err)
		}
		localPort := lis.Addr().(*net.TCPAddr).Port
		frpCancel, err = frpclient.RunPublishProxies(context.Background(), common, id, secret, localPort)
		if err != nil {
			return fmt.Errorf("frp proxies: %w", err)
		}
		defer frpCancel()
		fmt.Fprintf(os.Stdout, "Forwarding ID: %s\n", id)
		fmt.Fprintf(os.Stdout, "Secret: %s\n", secret)
		fmt.Fprintf(os.Stdout, "Share for mount: %s:%s\n", id, secret)
	}

	fmt.Fprintf(os.Stdout, "Listening on %s\n", lis.Addr().String())
	return source.Run(context.Background(), lis, srv)
}

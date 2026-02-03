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
	publishAddr string
	publishFRP  string
)

var publishCmd = &cobra.Command{
	Use:   "publish <folder>",
	Short: "Publish a folder: start gRPC server in-process and serve it (tunnel the port to remote for mount)",
	RunE:  runPublish,
	Args:  cobra.ExactArgs(1),
}

func init() {
	publishCmd.Flags().StringVarP(&publishAddr, "addr", "a", ":50051", "Listen address (e.g. :50051)")
	publishCmd.Flags().StringVar(&publishFRP, "frp", "", "Register with FRP server; value is hostname:port:password. Env: REMOTEFS_FRP")
}

func runPublish(cmd *cobra.Command, args []string) error {
	folder := filepath.Clean(args[0])
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
	if publishFRP != "" {
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
	if publishFRP != "" {
		server, token, err := frpclient.FRPConnection(publishFRP)
		if err != nil {
			return fmt.Errorf("frp connection: %w", err)
		}
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
		fmt.Fprintf(os.Stdout, "Forwarding token: %s:%s\n", id, secret)
	}

	fmt.Fprintf(os.Stdout, "Listening on %s\n", lis.Addr().String())
	return source.Run(context.Background(), lis, srv)
}

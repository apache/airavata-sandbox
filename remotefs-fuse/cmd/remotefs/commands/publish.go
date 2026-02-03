package commands

import (
	"context"
	"fmt"
	"net"
	"os"
	"path/filepath"

	"github.com/spf13/cobra"

	"github.com/you/remotefs/internal/export"
	"github.com/you/remotefs/internal/source"
	pb "github.com/you/remotefs/proto/gen/remotefs"
)

var (
	publishFolder string
	publishAddr   string
)

var publishCmd = &cobra.Command{
	Use:   "publish [folder]",
	Short: "Publish a folder: start gRPC server in-process and serve it (tunnel the port to remote for mount)",
	RunE:  runPublish,
}

func init() {
	publishCmd.Flags().StringVarP(&publishFolder, "folder", "f", "", "Folder path to publish")
	publishCmd.Flags().StringVarP(&publishAddr, "addr", "a", ":50051", "Listen address (e.g. :50051)")
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

	host, port, err := net.SplitHostPort(publishAddr)
	if err != nil {
		return fmt.Errorf("invalid addr %q: %w", publishAddr, err)
	}
	if host == "" {
		host = "0.0.0.0"
	}
	addr := net.JoinHostPort(host, port)
	lis, err := net.Listen("tcp", addr)
	if err != nil {
		return err
	}
	fmt.Fprintf(os.Stdout, "Listening on %s\n", addr)
	return source.Run(context.Background(), lis, srv)
}

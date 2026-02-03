//go:build !linux

package commands

import (
	"fmt"

	"github.com/spf13/cobra"
)

func init() {
	mountCmd = &cobra.Command{
		Use:   "mount <mountpoint>",
		Short: "Mount remote export (Linux only)",
		RunE:  runMountStub,
		Args:  cobra.ExactArgs(1),
	}
	mountCmd.Flags().StringP("addr", "a", "", "gRPC server address (e.g. localhost:50051)")
	mountCmd.Flags().String("token", "", "Forwarding token from publish --frp (id:secret)")
	mountCmd.Flags().String("frp", "", "FRP connection when using --token: hostname:port:password. Env: REMOTEFS_FRP")
	mountCmd.Flags().Bool("allow-other", false, "Allow other users to access the mount")
}

func runMountStub(cmd *cobra.Command, args []string) error {
	return fmt.Errorf("mount is only supported on Linux; run this binary on the remote Linux machine")
}

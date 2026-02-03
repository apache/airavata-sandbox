//go:build !linux

package commands

import (
	"fmt"

	"github.com/spf13/cobra"
)

func init() {
	mountCmd = &cobra.Command{
		Use:   "mount",
		Short: "Mount remote export (Linux only)",
		RunE:  runMountStub,
	}
	mountCmd.Flags().StringP("mountpoint", "m", "", "Mount point")
	mountCmd.Flags().StringP("server", "s", "", "Server address (tunneled publish endpoint)")
	mountCmd.Flags().String("id", "", "Forwarding ID from publish --frp (or id:secret)")
	mountCmd.Flags().String("secret", "", "Secret from publish --frp")
	mountCmd.Flags().String("frp-server", "", "FRP server when using --id. Env: REMOTEFS_FRP_SERVER")
	mountCmd.Flags().String("frp-token", "", "FRP token when using --id. Env: REMOTEFS_FRP_TOKEN")
	mountCmd.Flags().Bool("allow-other", false, "Allow other users to access the mount")
}

func runMountStub(cmd *cobra.Command, args []string) error {
	return fmt.Errorf("mount is only supported on Linux; run this binary on the remote Linux machine")
}

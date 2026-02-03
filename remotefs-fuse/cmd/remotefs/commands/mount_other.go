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
}

func runMountStub(cmd *cobra.Command, args []string) error {
	return fmt.Errorf("mount is only supported on Linux; run this binary on the remote Linux machine")
}

package commands

import (
	"github.com/spf13/cobra"
)

var rootCmd = &cobra.Command{
	Use:   "remotefs",
	Short: "Publish a folder over gRPC (tunnel to remote) and mount it remotely via FUSE",
}

func init() {
	rootCmd.AddCommand(publishCmd)
	rootCmd.AddCommand(mountCmd)
}

func Execute() error {
	return rootCmd.Execute()
}

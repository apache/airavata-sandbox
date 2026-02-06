package main

import (
	"log"
	"os"

	"github.com/apache/airavata-sandbox/remotefs-fuse/cmd/remotefs/commands"
)

func main() {
	if err := commands.Execute(); err != nil {
		log.Fatal(err)
	}
	os.Exit(0)
}

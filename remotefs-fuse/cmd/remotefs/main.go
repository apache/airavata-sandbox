package main

import (
	"log"
	"os"

	"github.com/you/remotefs/cmd/remotefs/commands"
)

func main() {
	if err := commands.Execute(); err != nil {
		log.Fatal(err)
	}
	os.Exit(0)
}

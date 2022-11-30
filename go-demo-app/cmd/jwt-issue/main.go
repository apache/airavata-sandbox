package main

import (
	"fmt"
	"os"

	"github.com/airavata-sandbox/go-demo-app/helper"
)

func main() {
	issuer, err := helper.NewIssuer(os.Args[1])
	if err != nil {
		fmt.Printf("unable to create issuer: %v\n", err)
		os.Exit(1)
	}

	token, err := issuer.IssueToken("admin", []string{"admin", "basic"})
	if err != nil {
		fmt.Printf("unable to issue token: %v\n", err)
		os.Exit(1)
	}

	fmt.Println(token)
}
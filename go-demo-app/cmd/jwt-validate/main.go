package main

import (
	"fmt"
	"os"

	"github.com/airavata-sandbox/go-demo-app/helper"
)

func main() {
	v, err := helper.NewValidator(os.Args[1])
	if err != nil {
		fmt.Printf("unable to create validator: %v\n", err)
		os.Exit(1)
	}

	token, err := v.GetToken(os.Args[2])
	if err != nil {
		fmt.Printf("unable to get validated token: %v\n", err)
		os.Exit(1)
	}

	fmt.Println(token.Claims)
}
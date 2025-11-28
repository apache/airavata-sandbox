package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
)

// Response represents the workload lease API response
type Response struct {
	AiravataExperimentID     string  `json:"airavataExperimentId"`
	AiravataExperimentStatus *string `json:"airavataExperimentStatus"`
	ID                       string  `json:"id"`
	LastUpdated              int64   `json:"lastUpdated"`
	Status                   string  `json:"status"`
	WorkConfig               string  `json:"workConfig"`
	WorkloadID               string  `json:"workloadId"`
}

func main() {
	// Read experiment ID from environment variable or use default
	experimentId := os.Getenv("EXPERIMENT_ID")
	if experimentId == "" {
		experimentId = "exp001"
	}

	// Read workload ID from environment variable or use default
	workloadId := os.Getenv("WORKLOAD_ID")
	if workloadId == "" {
		workloadId = "ff8c561f-aa49-4920-b324-c30fe0610610"
	}

	// Read base URL from environment variable or use default
	baseURL := os.Getenv("BASE_URL")
	if baseURL == "" {
		baseURL = "http://localhost:8080"
	}

	apiURL := baseURL + "/api/workload/lease/" + workloadId + "/" + experimentId

	// Make the HTTP GET request
	resp, err := http.Get(apiURL)
	if err != nil {
		log.Fatalf("Error making request: %v", err)
	}
	defer resp.Body.Close()

	// Check if the request was successful
	if resp.StatusCode == http.StatusNoContent {
		log.Fatalf("Error: Resource not found (204 No Content)")
	}

	if resp.StatusCode != http.StatusOK {
		log.Fatalf("Request failed with status: %s", resp.Status)
	}

	// Read the response body
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		log.Fatalf("Error reading response: %v", err)
	}

	// Print raw JSON response
	fmt.Println("Raw JSON Response:")
	fmt.Println(string(body))
	fmt.Println()

	// Parse JSON into struct
	var data Response
	if err := json.Unmarshal(body, &data); err != nil {
		log.Fatalf("Error parsing JSON: %v", err)
	}

	// Print parsed data
	fmt.Println("Parsed Data:")
	fmt.Printf("ID: %s\n", data.ID)
	fmt.Printf("Workload ID: %s\n", data.WorkloadID)
	fmt.Printf("Airavata Experiment ID: %s\n", data.AiravataExperimentID)
	fmt.Printf("Status: %s\n", data.Status)
	fmt.Printf("Work Config: %s\n", data.WorkConfig)
	fmt.Printf("Last Updated: %d\n", data.LastUpdated)
	if data.AiravataExperimentStatus != nil {
		fmt.Printf("Airavata Experiment Status: %s\n", *data.AiravataExperimentStatus)
	} else {
		fmt.Println("Airavata Experiment Status: null")
	}
}

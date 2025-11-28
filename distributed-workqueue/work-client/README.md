# Development build
go run main.go

# Build for your current platform
go build -o work-client

# Build for specific platforms by setting GOOS and GOARCH
# Linux (64-bit)
GOOS=linux GOARCH=amd64 go build -o work-client-linux-amd64

# Windows (64-bit)
GOOS=windows GOARCH=amd64 go build -o work-client-windows-amd64.exe

# macOS (Intel)
GOOS=darwin GOARCH=amd64 go build -o work-client-darwin-amd64

# macOS (Apple Silicon)
GOOS=darwin GOARCH=arm64 go build -o work-client-darwin-arm64

# Linux (ARM64)
GOOS=linux GOARCH=arm64 go build -o work-client-linux-arm64
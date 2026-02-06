.PHONY: proto build build-darwin build-linux build-all all test unit-test verify

proto:
	mkdir -p proto/gen/remotefs
	protoc --go_out=. --go_opt=module=github.com/apache/airavata-sandbox/remotefs-fuse \
		--go-grpc_out=. --go-grpc_opt=module=github.com/apache/airavata-sandbox/remotefs-fuse \
		proto/remotefs.proto

build:
	go build -o bin/remotefs ./cmd/remotefs

build-darwin:
	GOOS=darwin GOARCH=amd64 go build -o bin/remotefs-darwin-amd64 ./cmd/remotefs
	GOOS=darwin GOARCH=arm64 go build -o bin/remotefs-darwin-arm64 ./cmd/remotefs

build-linux:
	GOOS=linux GOARCH=amd64 go build -o bin/remotefs-linux-amd64 ./cmd/remotefs
	GOOS=linux GOARCH=arm64 go build -o bin/remotefs-linux-arm64 ./cmd/remotefs

build-all: build-darwin build-linux

all: build

unit-test:
	go test ./...

# Docker integration: build, compose up, verify script; updates STATUS.md.
verify: build
	docker compose -f compose.yml build
	docker compose -f compose.yml up -d
	@echo "Waiting for services and mount..."
	@sleep 20
	@./scripts/verify.sh > .verify.log 2>&1; r=$$?; \
	echo "# Verification Status" > STATUS.md; \
	echo "" >> STATUS.md; \
	echo "**Last run:** $$(date -u +%Y-%m-%dT%H:%M:%SZ)" >> STATUS.md; \
	echo "**Result:** $$(if [ $$r -eq 0 ]; then echo 'SUCCESS'; else echo 'FAILURE'; fi)" >> STATUS.md; \
	echo "" >> STATUS.md; \
	echo "## Verify script output" >> STATUS.md; \
	echo "" >> STATUS.md; \
	echo '```' >> STATUS.md; \
	cat .verify.log >> STATUS.md; \
	echo '```' >> STATUS.md; \
	rm -f .verify.log; \
	exit $$r

# Run unit tests then Docker verify
test: unit-test verify

.PHONY: proto build all test unit-test verify

proto:
	mkdir -p proto/gen/remotefs
	protoc --go_out=. --go_opt=module=github.com/you/remotefs \
		--go-grpc_out=. --go-grpc_opt=module=github.com/you/remotefs \
		proto/remotefs.proto

build: proto
	go build -o bin/remotefs ./cmd/remotefs

all: build

# Unit/integration tests (no Docker)
unit-test: proto
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

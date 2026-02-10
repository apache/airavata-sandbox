FROM golang:1.24-bookworm AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o /remotefs ./cmd/remotefs

FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    fuse3 \
    libfuse3-3 \
    netcat-openbsd \
    && rm -rf /var/lib/apt/lists/*
RUN ln -sf /usr/bin/fusermount3 /bin/fusermount || true
RUN mkdir -p /mnt/remote
COPY --from=builder /remotefs /remotefs
ENTRYPOINT ["/remotefs"]

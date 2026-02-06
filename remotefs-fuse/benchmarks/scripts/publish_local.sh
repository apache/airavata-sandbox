#!/bin/bash
# Start remotefs publish with FRP on local machine
# Outputs the forwarding token for use by remote hosts
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
DATA_DIR="${DATA_DIR:-/tmp/remotefs-benchmark-data}"
FRP_SERVER="${FRP_SERVER:-hub.dev.cybershuttle.org:7000:mysecret}"
REMOTEFS_BIN="${REMOTEFS_BIN:-$SCRIPT_DIR/../../bin/remotefs}"

# Check if data directory exists
if [ ! -d "$DATA_DIR" ]; then
    echo "Error: Data directory $DATA_DIR does not exist"
    echo "Run generate_test_data.sh first"
    exit 1
fi

# Check if remotefs binary exists
if [ ! -x "$REMOTEFS_BIN" ]; then
    # Try platform-specific binaries
    if [ -x "$SCRIPT_DIR/../../bin/remotefs-darwin-arm64" ]; then
        REMOTEFS_BIN="$SCRIPT_DIR/../../bin/remotefs-darwin-arm64"
    elif [ -x "$SCRIPT_DIR/../../bin/remotefs-darwin-amd64" ]; then
        REMOTEFS_BIN="$SCRIPT_DIR/../../bin/remotefs-darwin-amd64"
    else
        echo "Error: remotefs binary not found at $REMOTEFS_BIN"
        echo "Run 'make build' or 'make build-all' first"
        exit 1
    fi
fi

echo "=== Starting remotefs publish ==="
echo "Data directory: $DATA_DIR"
echo "FRP server: $FRP_SERVER"
echo "Binary: $REMOTEFS_BIN"
echo ""

# Start publish with FRP - this will output the forwarding token
exec "$REMOTEFS_BIN" publish "$DATA_DIR" --frp "$FRP_SERVER"

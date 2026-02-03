#!/bin/sh
set -e
# Wait for publish server to be up, then mount (point at tunneled publish endpoint).
sleep 5
exec /remotefs mount -s publish:50051 -m /mnt/remote

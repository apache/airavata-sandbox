#!/bin/sh
set -e
# Wait for publish server to be up, then mount (point at tunneled publish endpoint).
sleep 5
exec /remotefs mount /mnt/remote --addr publish:50051

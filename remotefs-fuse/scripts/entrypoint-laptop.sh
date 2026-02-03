#!/bin/sh
set -e
# Run publish (gRPC server in-process); tunnel this port to remote for mount.
exec /remotefs publish -f /export/data -addr 0.0.0.0:50051

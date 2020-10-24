#!/bin/bash

echo "Starting envoy proxy"
echo "Redirect HTTP/1.1 requests on 8080 to gRPC 8081"

docker run -it --rm \
  --network=host \
  --name=envoy-proxy \
  -v "$(pwd)/envoy.yml:/etc/envoy/envoy.yaml:ro"  \
  -p 8080:8080 \
  -p 9901:9901 \
  envoyproxy/envoy:v1.15.0



#!/bin/bash

SCRIPT_DIR=$(dirname `readlink -fm $0`)
PROTO_SRC_DIR="$(dirname ${SCRIPT_DIR})/proto/src/main/proto"
PROTO_OUT_DIR="${SCRIPT_DIR}/src/proto"

if [[ -d "${PROTO_OUT_DIR}" ]]; then
    echo "Cleaning stale files"
    for file in "${PROTO_OUT_DIR}"/*; do
      rm -rf "${file}"
    done
fi

echo "SCRIPT_DIR=${SCRIPT_DIR}"
echo "PROTO_SRC_DIR=${PROTO_SRC_DIR}"

for f in `ls ${PROTO_SRC_DIR}/*`; do
    echo "${f}"
done

protoc \
    --js_out=import_style=commonjs,binary:${PROTO_OUT_DIR}/ \
    --grpc-web_out=import_style=typescript,mode=grpcwebtext:${PROTO_OUT_DIR}/ \
    --plugin=protoc-gen-grpc=./node_modules/.bin/grpc_tools_node_protoc_plugin \
    -I ${PROTO_SRC_DIR} \
    ${PROTO_SRC_DIR}/*.proto


# Below section of code is to disable the below error 
# 'proto' is not defined
# from generated files by disabling eslint
for f in "${PROTO_OUT_DIR}"/*.js
do
    echo '/* eslint-disable */' | cat - "${f}" > temp && mv temp "${f}"
done

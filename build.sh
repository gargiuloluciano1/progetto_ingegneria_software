#!/usr/bin/env bash

# run as sudo
#

DOCKER_BASE_IMAGE=ingsof-image
DOCKER_BUILD_IMAGE=ingsof-build

# create base image
function create_base_image {
cat <<EOF | docker build -t ${DOCKER_BASE_IMAGE} -
FROM ubuntu:22.04 AS image
RUN apt-get update && apt-get install -y openjdk-21-jre openjdk-21-jdk
RUN mkdir app
RUN mkdir app/build
EOF
}

function delete-base-image() {
docker image rm ${DOCKER_BASE_IMAGE}
}

# if image exists
if ! docker image ls | grep ${DOCKER_BASE_IMAGE} >/dev/null; then
    create_base_image
else
    echo 'base image exists already'
fi

if docker image ls | grep ${DOCKER_BUILD_IMAGE} >/dev/null; then
    docker image rm ${DOCKER_BUILD_IMAGE}
fi
docker build -t ${DOCKER_BUILD_IMAGE} .

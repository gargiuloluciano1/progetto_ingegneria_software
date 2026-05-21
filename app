#!/usr/bin/env bash

export display=':0.0'
xhost +local:docker
DOCKER_RUNTIME_IMAGE=ingsof-build
docker run --rm -ti -e DISPLAY=$DISPLAY \
	    -v /tmp/.X11-unix:/tmp/.X11-unix \
	    --mount type=bind,src=./modules,dst=/app/modules \
	    --mount type=bind,src=./src/resources,dst=/app/src/resources \
	    ${DOCKER_RUNTIME_IMAGE}


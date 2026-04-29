#!/usr/bin/env bash

DOCKER_RUNTIME_IMAGE=ingsof-build
docker run --rm -ti -e DISPLAY=$DISPLAY \
	    -v /tmp/.X11-unix:/tmp/.X11-unix \
	    --mount type=bind,src=./modules,dst=/app/modules \
	    ${DOCKER_RUNTIME_IMAGE}


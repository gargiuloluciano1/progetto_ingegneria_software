#!/usr/bin/env bash

DOCKER_RUNTIME_IMAGE=ingsof-build
docker run --rm -ti -e DISPLAY=$DISPLAY \
	    -v /tmp/.X11-unix:/tmp/.X11-unix \
	    ${DOCKER_RUNTIME_IMAGE}


#!/usr/bin/env bash

# Expects the following environment variables to be set already:
# VERSION: from the source code build tool (e.g., Gradle). For example:
#          1.0.1
# DOCKER_TARGET_NAME: qualified with registry, but not with tag. For example:
#                    registry.clearsense.com:443/clearsense/ocs-referencesourceprofile-manager
# DOCKER_TAG: unique version tag for the image. For example:
#                    1.0.feature-DH-123-fix_stuff.a4ad6b0 (for a regular branch build)
#                    1.0.1 (for a release)
VERSION=$1
DOCKER_TARGET_NAME=$2
DOCKER_TAG=$3

DOCKER_TARGET="${DOCKER_TARGET_NAME}:${DOCKER_TAG}"
echo "Building Docker image target $DOCKER_TARGET"
export DOCKER_BUILDKIT=1
docker build --build-arg VERSION="$VERSION" -f external-assets/docker/Dockerfile -t "$DOCKER_TARGET" .

echo "Pushing Docker image to registry."
docker push "$DOCKER_TARGET"

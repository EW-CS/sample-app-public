#!/usr/bin/env bash

echo "Building Gradle project with Elastic APM dependency."
./gradlew clean build downloadAgentJars --refresh-dependencies

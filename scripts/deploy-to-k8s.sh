#!/usr/bin/env bash

# Expects the following environment variables to be set already:
# kubeNamespace
# helmReleasePrefix
# ENVIRONMENT_NAME
# DOCKER_TARGET_NAME: qualified with registry, but not with tag. For example:
#                    registry.clearsense.com:443/clearsense/ocp-example
# DOCKER_TAG: unique version tag for the image. For example:
#                    1.0.feature-DH-123-fix_stuff.a4ad6b0 (for a regular branch build)
#                    1.0.1 (for a release)

KUBENAMESPACE=$1
HELMRELEASEPREFIX=$2
ENVIRONMENT_NAME=$3
DOCKER_TARGET_NAME=$4
DOCKER_TAG=$5

echo "ENVIRONMENT_NAME: ${ENVIRONMENT_NAME}"

helmValuesName="${ENVIRONMENT_NAME}"
# Could override helmValuesName for special case condition below.
if [[ "${ENVIRONMENT_NAME}" == "dev" ]]; then
  deployClusterName=dev
  echo "Deploying to cluster: \"$deployClusterName\""
elif [[ "${ENVIRONMENT_NAME}" == "test" ]]; then
  deployClusterName=test
  echo "Deploying to cluster: \"$deployClusterName\""
elif [[ "${ENVIRONMENT_NAME}" == "uat" ]]; then
  # No UAT config for this project really exists yet, but to show multiple logical environments on same cluster.
  deployClusterName=test
  echo "Deploying to cluster: \"$deployClusterName\""
elif [[ "${ENVIRONMENT_NAME}" == "demo" ]]; then
  deployClusterName=test
  echo "Deploying to cluster: \"$deployClusterName\""
elif [[ "${ENVIRONMENT_NAME}" == "prod" ]]; then
  deployClusterName=prod
  echo "Deploying to cluster: \"$deployClusterName\""
else
    echo "Not deploying feature branch, done"
    exit 0
fi

releaseName="${HELMRELEASEPREFIX}-${helmValuesName}"
timeStamp=`TZ=GMT date +"%Y%m%d-%H%M%S"`
export KUBECONFIG="/opt/kubecfg/${deployClusterName}-kubecfg"
kubectl config use-context $deployClusterName

echo "Running Helm upgrade (or install) of release \"$releaseName\" using Kubernetes connection config in \"$KUBECONFIG\"."
helm3 upgrade --install \
  --values ./external-assets/helm/values-${helmValuesName}.yaml \
  "$releaseName" \
  ./external-assets/helm/ \
  -i --timeout 300s \
  --set image.repository="${DOCKER_TARGET_NAME}" \
  --set image.tag="${DOCKER_TAG}" \
  --set nameOverride="${HELMRELEASEPREFIX}" \
  --set fullnameOverride="$releaseName" \
  --namespace "${KUBENAMESPACE}" \
  --set-string timestamp="$timeStamp"

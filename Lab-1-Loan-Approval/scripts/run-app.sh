#!/usr/bin/env bash
set -euo pipefail

# Load local secrets from .env when present. The file is ignored by Git.
if [[ -f .env ]]; then
  set -a
  # shellcheck disable=SC1091
  source .env
  set +a
fi

: "${CAMUNDA_CLIENT_ID:?Set CAMUNDA_CLIENT_ID in .env or your shell}"
: "${CAMUNDA_CLIENT_SECRET:?Set CAMUNDA_CLIENT_SECRET in .env or your shell}"
: "${CAMUNDA_CLUSTER_ID:?Set CAMUNDA_CLUSTER_ID in .env or your shell}"
: "${CAMUNDA_REGION:?Set CAMUNDA_REGION in .env or your shell}"

mvn spring-boot:run

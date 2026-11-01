#!/usr/bin/env bash
set -euo pipefail

: "${CAMUNDA_REST_BASE_URL:?Set CAMUNDA_REST_BASE_URL to the cluster REST API base ending in /v2}"
: "${CAMUNDA_ACCESS_TOKEN:?Set CAMUNDA_ACCESS_TOKEN to a valid OAuth access token}"

TEST_FILE="${1:-test-data/approved.json}"
if [[ ! -f "$TEST_FILE" ]]; then
  echo "Test data file not found: $TEST_FILE" >&2
  exit 1
fi

PAYLOAD="$(python - "$TEST_FILE" <<'PY'
import json, sys
with open(sys.argv[1], encoding="utf-8") as f:
    variables = json.load(f)
print(json.dumps({"processDefinitionId": "LoanApprovalProcess", "variables": variables}))
PY
)"

curl --fail-with-body --silent --show-error \
  --request POST "${CAMUNDA_REST_BASE_URL%/}/process-instances" \
  --header "Authorization: Bearer ${CAMUNDA_ACCESS_TOKEN}" \
  --header "Content-Type: application/json" \
  --header "Accept: application/json" \
  --data "$PAYLOAD"
echo

# Deployment and run guide — Camunda 8 SaaS

## A. Prerequisites

- Java JDK 21.
- Maven 3.9+.
- A Camunda 8.10 SaaS cluster and a client credential with the required process deployment / execution access for the worker.
- Access to Web Modeler, Tasklist, Operate, and Identity.
- A temporary HTTPS tunnel tool such as Cloudflare Tunnel (`cloudflared`) or ngrok for the local mock API.

## B. Create / prepare the SaaS connection

1. Sign in to Camunda 8 SaaS and create or open your 8.10 cluster.
2. In the cluster's connection information, obtain the **Client ID**, **Client Secret**, **Cluster ID**, and **Region**.
3. Keep the client secret private. Do not commit it to Git or include it in screenshots.
4. Create the `loan-reviewers` group in Identity and add the user who will perform manual review. Candidate groups in newer Camunda versions refer to group IDs, not display names.

## C. Deploy the models and form

1. Open the Web Modeler workspace and project for your cluster.
2. Add `bpmn/loan-approval.bpmn`, `dmn/loan-eligibility.dmn`, and `forms/loan-review-form.form` to the same project.
3. Open the BPMN file and verify that the REST service task is shown as **Send REST Request** / REST Connector. If the connector template is not applied in the canvas, use the properties panel's Template selector and apply the built-in REST connector template; keep the XML inputs and task headers from this project.
4. Deploy **all three resources together** in one deployment. The BPMN uses `bindingType="deployment"` for both the DMN decision and linked form.
5. Confirm deployment in Operate / the deployment view.

## D. Configure environment variables and run the Spring Boot worker/API

From the project root, copy `.env.example` to `.env` and fill in the credentials from your cluster. Do not commit `.env`. The provided `scripts/run-app.sh` loads `.env` automatically. You can also export variables directly in your shell.

Linux/macOS shell example:

```bash
export CAMUNDA_CLIENT_ID="your-client-id"
export CAMUNDA_CLIENT_SECRET="your-client-secret"
export CAMUNDA_CLUSTER_ID="your-cluster-id"
export CAMUNDA_REGION="your-region"
mvn clean test
./scripts/run-app.sh
```

Windows PowerShell example:

```powershell
$env:CAMUNDA_CLIENT_ID="your-client-id"
$env:CAMUNDA_CLIENT_SECRET="your-client-secret"
$env:CAMUNDA_CLUSTER_ID="your-cluster-id"
$env:CAMUNDA_REGION="your-region"
mvn clean test
mvn spring-boot:run
```

The application starts the worker and exposes the synthetic API on port 8081. Check `http://localhost:8081/api/health` locally. The worker subscribes to job type `validate-applicant`.

## E. Make the mock API reachable by the SaaS REST Connector

A cloud-hosted connector cannot call your laptop's `localhost`. Start a temporary tunnel to port 8081. For example, with Cloudflare Tunnel:

```bash
cloudflared tunnel --url http://localhost:8081
```

Use the HTTPS URL printed by the tool as `creditApiUrl` in each process-start JSON. Remove any trailing slash. The URL is temporary and may change when the tunnel restarts.

For ngrok, the equivalent is:

```bash
ngrok http 8081
```

Only use synthetic data. Stop the tunnel when the exercise is complete.

## F. Start test instances

The Orchestration Cluster REST API exposes `POST /v2/process-instances`. This project includes a script that wraps a test JSON file as the `variables` object:

```bash
export CAMUNDA_REST_BASE_URL="your-cluster-rest-base-url-ending-in-/v2"
export CAMUNDA_ACCESS_TOKEN="your-short-lived-oauth-access-token"
./scripts/start-instance.sh test-data/approved.json
```

The base URL and OAuth token must come from your cluster's connection / client-credentials setup. The script uses the latest active version of `LoanApprovalProcess`. Replace `creditApiUrl` in the test JSON with the current HTTPS tunnel URL before starting. The API request shape follows Camunda's Create Process Instance endpoint.

After starting, watch the process instance in Operate and confirm that variables are created at the validation, connector, and DMN steps. For a manual-review case, open Tasklist, select the `loan-reviewers` group, complete the form, and verify the final route.

## G. Demonstrate worker / connector incident recovery

### Worker failure

1. Start `test-data/worker-failure.json`.
2. The worker intentionally throws `SIMULATED_WORKER_FAILURE` and consumes its configured retries.
3. In Operate, open the incident and inspect the failed `Validate applicant details` job.
4. Modify `simulateWorkerFailure` to `false` on the process instance.
5. Retry the failed job. The worker should now complete and the process should continue.

### Credit API failure

1. Start `test-data/api-failure.json`.
2. The mock API intentionally returns HTTP 503. The REST Connector retries and then creates an incident.
3. In Operate, inspect the connector activity and the error details.
4. Modify `simulateCreditApiFailure` to `false` on the process instance.
5. Retry the failed connector job. It should call the API successfully and continue to DMN.

Exact Operate menu labels can vary by cluster generation. Use the process-instance modification / incident retry actions available in your cluster UI.

## H. Evidence required for submission

Capture screenshots from your actual cluster (do not use sample or placeholder images):

- Deployed BPMN model and deployed DMN table.
- Web Modeler form linked to the manual-review user task.
- Successful approval and rejection instances in Operate.
- A pending manual-review task in Tasklist and its completed outcome.
- Worker incident, corrected variable, and successful retry.
- REST Connector HTTP 503 incident, corrected variable, and successful retry.
- Worker terminal output showing job activation / validation completion.

Save these in `evidence/` and attach the short implementation report when submitting.

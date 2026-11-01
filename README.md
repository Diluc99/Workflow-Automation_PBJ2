# Lab 1 — End-to-End Loan Approval Automation (Camunda 8)

**Submitted by:** Shivam Kumar Jha  
**Register Number:** RA2411056010039

A complete educational implementation for the Lab 1 requirements: BPMN orchestration, DMN decisioning, a Java/Spring Boot external worker, a REST Connector, a synthetic credit-assessment API, process variables, a Tasklist manual-review task, Operate monitoring, and incident-recovery scenarios.

> **Important:** This is a synthetic teaching project. It is not a real lender, credit bureau, or production underwriting system. Use only synthetic data.

## Quick start

1. Read [`docs/DEPLOYMENT-GUIDE.md`](docs/DEPLOYMENT-GUIDE.md) before starting. The project targets Camunda 8.10 SaaS, Java 21, and the Camunda Spring Boot 3 Starter 8.10.3.
2. Deploy `bpmn/loan-approval.bpmn`, `dmn/loan-eligibility.dmn`, and `forms/loan-review-form.form` **together** to the same cluster.
3. Create the Identity group with ID `loan-reviewers` and add your Tasklist user.
4. Copy `.env.example` to `.env` and fill in the four Camunda credentials (or export them in your shell). `.env` is Git-ignored.
5. Run `mvn clean test`, then `./scripts/run-app.sh` (the script loads `.env` automatically when present).
6. Check `http://localhost:8081/api/health`.
7. Start an HTTPS tunnel to port 8081 and set the resulting URL as process variable `creditApiUrl`.
8. Start a process instance using `scripts/start-instance.sh test-data/approved.json` after setting the REST base URL and OAuth access token, or use the cluster UI.

## Visual documentation

The `images/` directory contains original project diagrams for the architecture, workflow stages, DMN decision logic, and test coverage. These are authored explanatory figures—not screenshots of a live Camunda deployment. Runtime evidence must be captured from your own cluster and added to `evidence/`.

## Project contents

- `bpmn/loan-approval.bpmn` — executable BPMN process with validation, REST Connector, DMN call, gateways, and a Camunda user task.
- `dmn/loan-eligibility.dmn` — executable FIRST-hit decision table.
- `forms/loan-review-form.form` — Tasklist form that writes `reviewOutcome` and `reviewNotes`.
- `src/main/java/` — Spring Boot app, validation worker, synthetic API, DTOs, and services.
- `src/test/java/` — unit tests for validation and the mock credit-assessment service.
- `test-data/` — start-variable sets for approval, rejection, manual review, invalid input, API incident, and worker incident.
- `images/` — architecture, workflow, decision logic, and test coverage figures.
- `docs/` — architecture, variable dictionary, REST Connector configuration, test plan, deployment guide, implementation-report template, viva notes, and official Camunda documentation links.
- `scripts/start-instance.sh` — starts a process instance using the Camunda Orchestration Cluster REST API.

## Mock API

`GET /api/health` — health check.

`POST /api/credit-assessments` — synthetic credit assessment. Example request:

```json
{
  "applicantId": "APP-1001",
  "monthlyIncome": 80000,
  "requestedLoanAmount": 800000,
  "simulatedCreditScore": 760,
  "simulateCreditApiFailure": false
}
```

The response contains `creditScore`, `riskBand`, `assessmentReference`, `assessmentStatus`, `provider`, and `assessedAt`.

Set `simulateCreditApiFailure` to `true` to return HTTP 503 for incident demonstration.

## Worker failure switch

Set the process variable `simulateWorkerFailure` to `true` to make the validation worker throw an exception. After retries are exhausted, set it to `false` in Operate and retry the failed job.

## Build and run

Requirements: JDK 21 and Maven 3.9+.

```bash
mvn clean test
./scripts/run-app.sh
```

The app requires the Camunda SaaS environment variables. See `.env.example` and the deployment guide. Do not put your secret into source control.

## Important SaaS note

The REST Connector runs in Camunda's cloud environment. It cannot reach a local-only `localhost:8081` service. The `creditApiUrl` process variable must point to a publicly reachable HTTPS base URL forwarding to this app. The deployment guide shows a temporary tunnel option.

## Honest delivery boundary

The project files and test fixtures are provided here. No live Camunda cluster is connected from this build environment, so actual deployment, Tasklist completion, Operate incidents, and runtime screenshots must be produced in your own SaaS cluster. Do not submit fabricated screenshots.

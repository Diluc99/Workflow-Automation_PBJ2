# Architecture and execution flow

## Components

1. **Camunda 8 SaaS** executes the BPMN process and DMN decision.
2. **ApplicantValidationWorker** is a Spring Boot external job worker subscribed to `validate-applicant`.
3. **Credit Assessment REST Connector** makes an HTTP POST to the synthetic credit API. Its task type is `io.camunda:http-json:1`.
4. **Synthetic Credit API** is served by this same Spring Boot application at `POST /api/credit-assessments`.
5. **DMN** classifies the application as `APPROVED`, `REJECTED`, or `MANUAL_REVIEW`.
6. **Tasklist** hosts the linked `loan-review-form.form` for the `loan-reviewers` candidate group.
7. **Operate** is used to inspect variables, incidents, and recovery.

## Execution flow

`Start -> Validate applicant -> Validation gateway -> Credit REST Connector -> DMN business-rule task -> Decision gateway -> Approved / Rejected / Manual review -> Review outcome gateway -> Approved / Rejected`

Validation failures go directly to the invalid-application end event. Credit API and worker failures are deliberately left as job incidents so they can be inspected and retried after the cause is corrected.

## SaaS networking boundary

The Spring Boot application runs on the student's computer. The worker makes an outbound connection to Camunda SaaS. The REST Connector, however, runs in Camunda's connector runtime. A SaaS connector cannot use `http://localhost:8081` to reach a service running on the student's laptop. Therefore, set the process variable `creditApiUrl` to a public HTTPS base URL that forwards to port 8081 (for example, a temporary Cloudflare Tunnel or ngrok URL).

Use only synthetic test data. Do not expose real applicant data, private credentials, or an unrestricted production API through a temporary tunnel.

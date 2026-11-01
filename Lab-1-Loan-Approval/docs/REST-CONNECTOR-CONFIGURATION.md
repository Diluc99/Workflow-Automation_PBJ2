# REST Connector configuration

The BPMN service task `Task_CreditAssessment` uses the Camunda REST Connector with task type `io.camunda:http-json:1`. It is modeled with the Camunda 8.10 HTTP JSON element-template identifier `io.camunda.connectors.HttpJson.v2`.

| Connector property | Value / expression |
|---|---|
| Method | `POST` |
| URL | `= creditApiUrl + "/api/credit-assessments"` |
| Authentication | None (mock API only) |
| Content-Type | `application/json` |
| Body | FEEL context containing `applicantId`, `monthlyIncome`, `requestedLoanAmount`, `simulatedCreditScore`, and `simulateCreditApiFailure` |
| Connection timeout | 10 seconds |
| Read timeout | 20 seconds |
| Retries | 3 |
| Retry backoff | 5 seconds |
| Result expression | Extracts `creditScore`, `riskBand`, `assessmentReference`, `assessmentStatus`, `creditProvider`, and `creditAssessedAt` from `response.body` |

The connector returns the mapped result fields as process variables. The REST response object is available only in the connector's response-expression context. HTTP 503 is treated as a connector failure and can be inspected in Operate.

The API URL is deliberately a process variable. For Camunda SaaS, it must be the public HTTPS address of the mock API. `localhost` does not refer to the student's computer from the connector runtime.

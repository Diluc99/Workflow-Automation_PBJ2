# Process-variable dictionary

| Variable | Type | Provided by | Purpose |
|---|---|---|---|
| `applicantId` | string | Start input | Synthetic ID in `APP-1234` format |
| `applicantName` | string | Start input | Applicant display name |
| `age` | number | Start input | Structural validation, 18–70 |
| `monthlyIncome` | number | Start input | Monthly income in INR; DMN input |
| `requestedLoanAmount` | number | Start input | Requested principal in INR |
| `employmentType` | string | Start input | Descriptive applicant information |
| `simulatedCreditScore` | number | Start input | Test fixture passed to the mock credit API (300–900) |
| `creditApiUrl` | string | Start input | Public HTTPS base URL of the mock API, without a trailing slash |
| `simulateWorkerFailure` | boolean | Start input | `true` intentionally makes the validation worker fail |
| `simulateCreditApiFailure` | boolean | Start input | `true` makes the mock API respond with HTTP 503 |
| `validationPassed` | boolean | Worker output | Structural validation result |
| `validationMessage` | string | Worker output | Human-readable validation summary |
| `validationErrors` | list of strings | Worker output | Validation problems, if any |
| `loanToIncomeRatio` | number | Worker output | `requestedLoanAmount / monthlyIncome` |
| `creditScore` | number | REST Connector output | Returned synthetic credit score; DMN input |
| `riskBand` | string | REST Connector output | `LOW`, `MEDIUM`, or `HIGH` |
| `assessmentReference` | string | REST Connector output | Synthetic assessment reference |
| `assessmentStatus` | string | REST Connector output | API assessment status |
| `creditProvider` | string | REST Connector output | Mock provider label |
| `creditAssessedAt` | string | REST Connector output | API assessment timestamp |
| `loanDecisionResult` | object | DMN output | DMN result map; read `loanDecisionResult.decision` |
| `reviewOutcome` | string | Tasklist form | `APPROVE` or `REJECT` |
| `reviewNotes` | string | Tasklist form | Optional reviewer notes |

The REST Connector uses reserved variable names internally (`body`, `url`, `method`, `headers`, `authentication`, `queryParameters`, `connectionTimeoutInSeconds`, and `readTimeoutInSeconds`). Do not create process variables with those names.

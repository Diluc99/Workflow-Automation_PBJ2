# Test cases and expected routing

Amounts are INR. These values are synthetic teaching fixtures, not lending recommendations.

| ID | Input summary | Expected path | Expected final state |
|---|---|---|---|
| TC-01 | Score 760, income 80,000, loan 800,000 (ratio 10) | Validation -> credit API -> DMN APPROVED | Loan approved |
| TC-02 | Score 520, income 25,000, loan 400,000 | DMN first rule: score below 550 | Loan rejected |
| TC-03 | Score 660, income 45,000, loan 700,000 (ratio 15.56) | DMN MANUAL_REVIEW -> Tasklist reviewer | Depends on reviewer selection |
| TC-03A | Score 760, income 12,000, loan 100,000 | DMN low-income rejection rule | Loan rejected |
| TC-03B | Score 760, income 15,000, loan 900,000 (ratio 60) | DMN high-ratio rejection rule | Loan rejected |
| TC-03C | Score 680, income 50,000, loan 500,000 (ratio 10) | DMN MANUAL_REVIEW | Depends on reviewer selection |
| TC-04 | Applicant ID malformed and age 17 | Validation false -> invalid end | Rejected - invalid application |
| TC-05 | `simulateCreditApiFailure=true` | REST Connector gets HTTP 503; retries then incident | Correct variable and retry |
| TC-06 | `simulateWorkerFailure=true` | Worker throws exception; retries then incident | Correct variable and retry |
| TC-07 | Complete manual-review form with APPROVE | Review gateway -> approved end | Loan approved |
| TC-08 | Complete manual-review form with REJECT | Review gateway default/reject path | Loan rejected |

## Test procedure

1. Deploy the BPMN, DMN, and form together to the same Camunda 8.10 cluster.
2. Start the Spring Boot app with valid SaaS credentials.
3. Expose the API through a temporary HTTPS tunnel and put its base URL into `creditApiUrl`.
4. Start a process instance with each JSON file in `test-data/` (replace the URL placeholder first).
5. For TC-03, open the manual review task in Tasklist, select an outcome, and complete the form.
6. For TC-05 / TC-06, wait for retries to exhaust, inspect the incident in Operate, use process-instance modification to change the corresponding simulation variable to `false`, then retry the failed job.
7. Record actual outcomes and capture genuine screenshots from your own running cluster for submission.

# Lab 1 build plan and design decisions

## Scope aligned to the assignment PDF

The deliverables cover: one BPMN process; one DMN decision table; a Java/Spring Boot worker; a REST Connector and synthetic API; process variables; gateway routing; a Tasklist manual-review user task; multiple test instances; Operate monitoring; and worker/API failure-recovery demonstrations.

## Chosen responsibilities

- Worker: validate applicant structure and compute loan-to-income ratio.
- REST Connector: call the mock credit API and map its JSON response into process variables.
- DMN: make the policy classification. The worker does not make the credit decision.
- Tasklist form: collect the human review outcome.
- Operate: inspect and recover incidents.

## Explicit assumptions

- Applicant ID format: `APP-` followed by four digits.
- Structural age range: 18–70 inclusive.
- Monthly income and requested principal must be positive.
- Low income is deliberately left to DMN, not structural validation.
- `loanToIncomeRatio = requestedLoanAmount / monthlyIncome`.
- DMN rules are invented educational thresholds, not real lending policy.
- Manual-review group ID is `loan-reviewers` and must be created in Identity.
- The API is local by default, but the SaaS REST Connector requires an externally reachable HTTPS base URL.
- The BPMN, DMN, and linked Camunda Form should be deployed together because both DMN and form bindings use `deployment`.

## Definition of done

- BPMN, DMN, and form XML/JSON parse successfully.
- Java source and tests are included.
- Nine synthetic test-data JSON files are included, covering approval, multiple rejection and manual-review paths, invalid input, API failure, and worker failure.
- Deployment, connector networking, manual task assignment, and incident recovery are explained.
- No fabricated runtime screenshots are included.

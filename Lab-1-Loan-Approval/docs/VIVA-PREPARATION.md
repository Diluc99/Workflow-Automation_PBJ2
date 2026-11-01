# Lab 1 viva preparation

## Core concepts

**1. What is BPMN?**
Business Process Model and Notation is a standard graphical notation used to model workflow orchestration. In this lab, BPMN defines sequence, service tasks, gateways, a business-rule task, and a human task.

**2. What is DMN?**
Decision Model and Notation represents repeatable decisions separately from process flow. The DMN table evaluates credit score, monthly income, and loan-to-income ratio and returns one classification.

**3. Why use an external job worker?**
The worker runs application code outside the workflow engine. Camunda creates a job with type `validate-applicant`; the Spring Boot worker fetches the process variables, validates them, and completes the job with output variables.

**4. Why use a REST Connector?**
The REST Connector performs the HTTP POST to the external credit-assessment API without writing a dedicated Java worker for that HTTP call. Its result expression maps response fields into process variables.

**5. What is a process variable?**
A process variable carries data between BPMN elements. Examples are `applicantId`, `validationPassed`, `creditScore`, `loanToIncomeRatio`, and `reviewOutcome`.

**6. What does an exclusive gateway do?**
It selects one outgoing sequence flow based on conditions. The validation gateway checks `validationPassed`; the decision gateway checks the DMN result; the review gateway checks the human outcome.

**7. What is Tasklist?**
Tasklist is the human-work interface. A user in the configured `loan-reviewers` group completes the linked form with `APPROVE` or `REJECT`.

**8. What is Operate?**
Operate is used to inspect process instances, variables, activity history, failed jobs, and incidents.

**9. What is an incident?**
An incident marks a workflow element that cannot proceed after its available retries are exhausted. The cause must be corrected before retrying the failed job.

**10. Why does the local API need a public URL?**
The REST Connector executes in Camunda's cloud environment. `localhost` would refer to the connector runtime, not the student's laptop. A temporary HTTPS tunnel gives the connector a reachable endpoint.

## Explain the DMN policy

The `FIRST` hit policy means the first matching rule wins. The first three rules reject low score, low income, or an extreme loan-to-income ratio. The fourth rule approves only when every approval threshold is met. The final catch-all rule sends all remaining structurally valid applications to manual review.

## Important limitation to state honestly

This is an educational simulator. The score is supplied as synthetic test input and the policy thresholds are invented for the assignment. It is not a real credit bureau, underwriting model, or financial recommendation.

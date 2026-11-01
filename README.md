# Camunda 8 Practical Assignment — Labs 1–3

**Course/Assignment:** PBJ-2 Practical Assignment  
**Project:** Camunda 8 — End-to-End Workflow Automation  
**Student Name:** Shivam Kumar Jha  
**Register Number:** RA2411056010039  

> This README is a consolidated guide for Labs 1, 2, and 3. Replace the marked implementation notes with the details and test results from your own Camunda 8 environment. Add screenshots only after capturing them from your running implementation.

---

## 1. Assignment Overview

The assignment requires three separate workflow implementations using Camunda 8:

1. **Lab 1 — Loan Approval Automation**
2. **Lab 2 — E-Commerce Order Processing and Fulfilment**
3. **Lab 3 — Hospital Appointment and Patient Triage Management**

Each lab combines BPMN orchestration, DMN decisions, Java/Spring Boot external job workers, REST integration or mock APIs, process variables, gateways, Tasklist human tasks, and Operate monitoring.

### Common implementation guidelines

- Model BPMN and DMN using Camunda 8 Modeler / Web Modeler.
- Use Java with Spring Boot for external job workers.
- Use REST Connectors or mock REST APIs for service integration.
- Deploy the required BPMN and DMN models and run multiple workflow instances.
- Use Tasklist for human tasks and Operate to inspect instances, variables, activity states, failures, and incidents.
- Use synthetic test data only.
- Do not include real payment credentials, secrets, or real patient data.
- Test normal, exceptional, and failure/recovery paths.
- Report actual outcomes; do not mark a scenario as passed until it has been executed.

---

## 2. Lab 1 — End-to-End Loan Approval Automation

### Objective

Build an end-to-end loan approval workflow that combines BPMN orchestration, DMN-based decision-making, applicant validation, a credit-assessment service, human review, monitoring, and incident recovery.

### Workflow requirements

1. Accept applicant details, requested loan amount, monthly income, and other relevant application information.
2. Use a DMN decision table to classify an application as **Approved**, **Rejected**, or **Manual Review** using documented educational rules.
3. Implement a Java/Spring Boot external job worker to validate applicant information and return validation results as process variables.
4. Configure a REST Connector to call a simulated or actual credit-assessment API. Use synthetic data.
5. Pass applicant details, validation results, credit-assessment results, and decision outcomes through process variables.
6. Use gateways to route the application based on the DMN result and service responses.
7. Assign a manual-review user task through Tasklist for applications requiring additional review.
8. Deploy the BPMN and DMN models and test multiple application instances with different inputs.
9. Use Operate to inspect execution, variables, and activity states. Intentionally cause a worker/service failure, diagnose it, correct the cause, and retry the failed job where supported.

### Minimum test scenarios

| ID | Scenario | Expected route / result | Actual result | Status |
|---|---|---|---|---|
| L1-T1 | Application meets approval criteria | Approved | Fill after execution | Not run |
| L1-T2 | Application fails eligibility criteria | Rejected | Fill after execution | Not run |
| L1-T3 | Application requires additional review | Manual Review / Tasklist | Fill after execution | Not run |
| L1-T4 | Credit API error or timeout | Error handling / incident path | Fill after execution | Not run |
| L1-T5 | Worker failure, correction, and retry | Workflow resumes successfully after correction | Fill after execution | Not run |

### Expected deliverables

- BPMN diagram and deployed process definition.
- DMN decision table with input/output definitions and sample evaluations.
- Spring Boot worker source code and configuration instructions.
- REST request/response example and process-variable list.
- Test-case table showing inputs, expected route, actual outcome, and status.
- Tasklist and Operate evidence, including incident investigation and recovery.
- Brief report describing architecture, implementation, results, and limitations.

### Implementation notes

- **BPMN file:** `TODO`
- **DMN file:** `TODO`
- **Worker module / run command:** `TODO`
- **Credit-assessment mock API:** `TODO`
- **Process variables:** `TODO`
- **Deployment details:** `TODO`

---

## 3. Lab 2 — End-to-End E-Commerce Order Processing and Fulfilment

### Objective

Build an order-processing workflow that validates an order, checks inventory, determines delivery priority, processes a simulated payment, and routes the order to fulfilment or review.

### Workflow requirements

1. Receive customer reference, product ID, quantity, delivery preference, and payment details or a payment token for a mock service.
2. Use a DMN decision table to determine order priority using order value, customer category, and delivery preference. Document the rules and outputs.
3. Implement a Java/Spring Boot external job worker to validate the order, check simulated inventory, and calculate the order total.
4. Configure a REST Connector to invoke a payment gateway API or mock payment service. Do not use real payment credentials.
5. Exchange order details, stock status, order total, payment status, and delivery priority through process variables.
6. Use exclusive gateways to route orders based on stock availability and payment outcome.
7. Create a Tasklist user task for manual verification of high-value or exceptional orders.
8. Deploy BPMN and DMN models and run multiple workflow instances covering normal and exceptional paths.
9. Monitor instances in Operate. Simulate a payment or inventory-service failure, inspect the incident, fix the underlying cause, and retry the failed job where supported.

### Minimum test scenarios

| ID | Scenario | Expected route / result | Actual result | Status |
|---|---|---|---|---|
| L2-T1 | Stock available and payment succeeds | Continue to fulfilment | Fill after execution | Not run |
| L2-T2 | Insufficient stock | Order does not proceed to fulfilment | Fill after execution | Not run |
| L2-T3 | Payment declined by mock service | Payment-failure route | Fill after execution | Not run |
| L2-T4 | High-value order | Manual review in Tasklist | Fill after execution | Not run |
| L2-T5 | Payment/inventory failure and recovery | Successful retry after corrective action | Fill after execution | Not run |

### Expected deliverables

- BPMN diagram and DMN priority decision table.
- Spring Boot worker source code and setup instructions.
- REST Connector configuration and sample API request/response.
- Process-variable dictionary and gateway-routing explanation.
- Test-case table with expected and actual results.
- Tasklist evidence for review tasks and Operate evidence for execution and incident recovery.
- Brief report documenting implementation and assumptions.

### Implementation notes

- **BPMN file:** `TODO`
- **DMN file:** `TODO`
- **Worker module / run command:** `TODO`
- **Inventory mock API:** `TODO`
- **Payment mock API:** `TODO`
- **Process variables:** `TODO`
- **Deployment details:** `TODO`

---

## 4. Lab 3 — End-to-End Hospital Appointment and Patient Triage Management

### Objective

Develop a hospital appointment workflow that registers a synthetic patient, applies predefined educational triage rules, checks appointment availability, routes cases for staff review, and supports monitoring and recovery.

**Important:** Use synthetic patient data and a mock scheduling API. The triage rules are for the educational workflow only and are not medical advice or a clinical decision system.

### Workflow requirements

1. Collect a synthetic patient reference, appointment preference, symptoms summary, and predefined urgency indicators.
2. Use a DMN decision table to classify requests as **Routine**, **Priority Review**, or **Urgent Assessment** using predefined educational rules.
3. Implement a Java/Spring Boot external job worker to validate registration fields and request appointment availability.
4. Configure a REST Connector to call a mock hospital scheduling API and retrieve available appointment slots.
5. Exchange patient reference, appointment preference, urgency category, available slots, and booking status through process variables.
6. Use gateways to route requests based on the simulated triage category and appointment availability.
7. Create a Tasklist user task for authorised staff review when manual intervention is required or no suitable slot is available.
8. Deploy and execute multiple workflow instances, then inspect process status, variables, and activity history in Operate.
9. Simulate a scheduling-service failure, inspect the incident, correct the cause, and retry the failed job where supported.

### Minimum test scenarios

| ID | Scenario | Expected route / result | Actual result | Status |
|---|---|---|---|---|
| L3-T1 | Routine request with an available slot | Appointment booking path | Fill after execution | Not run |
| L3-T2 | Priority request | Staff review path | Fill after execution | Not run |
| L3-T3 | Urgent-assessment category | Immediate human assessment in simulated workflow | Fill after execution | Not run |
| L3-T4 | No appointment slots available | Manual intervention / review | Fill after execution | Not run |
| L3-T5 | Scheduling API failure and recovery | Successful retry after corrective action | Fill after execution | Not run |

### Expected deliverables

- BPMN diagram and DMN decision table with documented educational rules.
- Spring Boot worker source code and mock scheduling API details.
- REST Connector configuration and sample request/response.
- Process-variable dictionary and routing explanation.
- Test-case table with expected and actual results.
- Tasklist and Operate evidence for workflow execution and incident recovery.
- Brief report describing implementation, results, assumptions, and limitations.

### Implementation notes

- **BPMN file:** `TODO`
- **DMN file:** `TODO`
- **Worker module / run command:** `TODO`
- **Mock scheduling API:** `TODO`
- **Process variables:** `TODO`
- **Deployment details:** `TODO`

---

## 5. Shared Process-Variable Dictionary Template

Complete this table with the exact variable names and types used in each implemented process.

| Lab | Variable name | Type | Purpose |
|---|---|---|---|
| 1 | `TODO` | `TODO` | Applicant / loan application data |
| 1 | `TODO` | `TODO` | Validation or credit-assessment result |
| 1 | `TODO` | `TODO` | DMN decision outcome |
| 2 | `TODO` | `TODO` | Order and customer data |
| 2 | `TODO` | `TODO` | Inventory / payment status |
| 2 | `TODO` | `TODO` | Order priority and fulfilment route |
| 3 | `TODO` | `TODO` | Synthetic patient and appointment preference |
| 3 | `TODO` | `TODO` | Triage category and available slots |
| 3 | `TODO` | `TODO` | Booking status / review route |

---

## 6. Deployment and Execution Record

Fill in the details after deploying and running the processes.

| Item | Lab 1 | Lab 2 | Lab 3 |
|---|---|---|---|
| Camunda environment / cluster | TODO | TODO | TODO |
| BPMN process ID | TODO | TODO | TODO |
| DMN decision ID | TODO | TODO | TODO |
| Deployment date | TODO | TODO | TODO |
| Worker started successfully | TODO | TODO | TODO |
| Mock API reachable | TODO | TODO | TODO |
| Test instances executed | TODO | TODO | TODO |
| Incident created and inspected | TODO | TODO | TODO |
| Retry / recovery verified | TODO | TODO | TODO |

---

## 7. Evidence Checklist

Add evidence captured from your own running Camunda implementation. Do not use placeholder images as proof of execution.

- [ ] BPMN model design for each lab.
- [ ] DMN model and sample decision result for each lab.
- [ ] Worker source code and successful startup output.
- [ ] REST Connector configuration and API request/response.
- [ ] Successful workflow instance in Operate.
- [ ] Tasklist user task and its completion, where applicable.
- [ ] Failure/incident inspection in Operate.
- [ ] Corrective action and successful retry/recovery.
- [ ] Test matrix with actual results and status.
- [ ] Brief implementation report for each lab.

Suggested evidence directory:

```text
evidence/
├── lab-1/
├── lab-2/
└── lab-3/
```

---

## 8. Suggested Repository Structure

Adapt this layout to the files actually present in your submission.

```text
camunda8-practical-assignment/
├── README.md
├── lab-1-loan-approval/
│   ├── bpmn/
│   ├── dmn/
│   ├── worker/
│   ├── api-examples/
│   └── report/
├── lab-2-ecommerce-fulfilment/
│   ├── bpmn/
│   ├── dmn/
│   ├── worker/
│   ├── api-examples/
│   └── report/
├── lab-3-hospital-triage/
│   ├── bpmn/
│   ├── dmn/
│   ├── worker/
│   ├── api-examples/
│   └── report/
└── evidence/
    ├── lab-1/
    ├── lab-2/
    └── lab-3/
```

---

## 9. Limitations and Assumptions

- This is an educational workflow assignment. Decision rules and mock services are illustrative and must be documented in the corresponding lab.
- Actual implementation details, deployment identifiers, execution results, and evidence must be completed from the running environment.
- Lab 2 must use a simulated payment service or a safe mock integration; no real payment credentials should be committed.
- Lab 3 must use synthetic patient data. Its educational triage logic must not be represented as a clinical recommendation.
- A scenario should be marked **Passed** only after the actual process execution confirms the expected outcome.

---

## 10. Final Submission Checklist

- [ ] Separate BPMN implementation for all three labs.
- [ ] Separate DMN decision model for all three labs.
- [ ] Java/Spring Boot worker source code and setup instructions.
- [ ] REST Connector / mock API configuration and examples.
- [ ] Process-variable dictionaries.
- [ ] Test matrices with actual outcomes.
- [ ] Tasklist and Operate evidence captured from the running environment.
- [ ] Incident investigation and recovery demonstrated where required.
- [ ] Brief report for each lab.
- [ ] README updated with final implementation specifics.
- [ ] No real secrets, payment credentials, or patient data included.

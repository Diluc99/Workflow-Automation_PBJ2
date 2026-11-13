# PBJ-2 Practical Assignment
## Camunda 8: End-to-End Workflow Automation

| Student Detail | Information |
|---|---|
| **Student Name** | **Shivam Kumar Jha** |
| **Register Number** | **RA2411056010039** |
| **Course / Assignment** | **PBJ-2 Practical Assignment** |
| **Project** | **Camunda 8 — End-to-End Workflow Automation** |

---

## Project Overview

This repository contains three separate Camunda 8 workflow implementations developed for the PBJ-2 Practical Assignment. Each lab combines BPMN process orchestration, DMN decision automation, Java/Spring Boot workers, REST-based service integration, process variables, human tasks, and workflow monitoring.

| Lab | Workflow | Main automation |
|---|---|---|
| **Lab 1** | End-to-End Loan Approval Automation | Validates loan applications, obtains a simulated credit assessment, applies decision rules, and routes applications to approval, rejection, or manual review. |
| **Lab 2** | End-to-End E-Commerce Order Processing and Fulfilment | Validates orders, checks simulated inventory, calculates totals, determines delivery priority, processes mock payments, and routes orders to fulfilment or review. |
| **Lab 3** | End-to-End Hospital Appointment and Patient Triage Management | Validates synthetic appointment requests, applies educational triage rules, checks mock appointment availability, and routes requests to booking or staff review. |

All three labs are independent workflow implementations. Each has its own BPMN and DMN models, forms, Spring Boot worker, mock REST API, documentation, and JSON test inputs.

## Technology Stack

- **Workflow engine:** Camunda 8 Self-Managed / Camunda 8 Run
- **Process modelling:** BPMN 2.0 using Camunda Modeler
- **Decision modelling:** DMN decision tables
- **Backend:** Java 17 and Spring Boot 3.5.6
- **Camunda Java client:** Camunda Spring Boot Starter 8.8.15
- **Build tool:** Apache Maven
- **Service integration:** Camunda REST Connector and mock REST APIs
- **Human workflow:** Camunda Tasklist
- **Monitoring and incidents:** Camunda Operate
- **Test data:** Synthetic JSON inputs

## Repository Structure

```text
Camunda-PBJ2-Practical/
│
├── README.md
├── .gitignore
│
├── lab1-loan-approval/
│   ├── camunda-models/
│   │   ├── loan-approval.bpmn
│   │   ├── loan-decision.dmn
│   │   ├── loan-application-form.form
│   │   └── loan-manual-review-form.form
│   ├── worker/
│   │   ├── pom.xml
│   │   └── src/
│   ├── docs/
│   │   ├── api-examples.md
│   │   ├── dmn-rules.md
│   │   ├── gateway-routing.md
│   │   ├── test-cases.md
│   │   └── variables.md
│   └── test-inputs/
│
├── lab2-order-processing/
│   ├── camunda-models/
│   │   ├── order-fulfilment.bpmn
│   │   ├── order-priority.dmn
│   │   ├── order-form.form
│   │   └── order-verification-form.form
│   ├── worker/
│   │   ├── pom.xml
│   │   └── src/
│   ├── docs/
│   │   ├── api-examples.md
│   │   ├── dmn-rules.md
│   │   ├── gateway-routing.md
│   │   ├── test-cases.md
│   │   └── variables.md
│   └── test-inputs/
│
└── lab3-patient-triage/
    ├── camunda-models/
    │   ├── patient-triage.bpmn
    │   ├── triage-category.dmn
    │   ├── triage-request-form.form
    │   ├── priority-review-form.form
    │   ├── urgent-assessment-form.form
    │   └── no-slot-review-form.form
    ├── worker/
    │   ├── pom.xml
    │   └── src/
    ├── docs/
    │   ├── api-examples.md
    │   ├── dmn-rules.md
    │   ├── routing.md
    │   ├── test-cases.md
    │   └── variables.md
    └── test-inputs/
```

Each `worker` directory is a separate Maven/Spring Boot application. The `docs` directory for each lab describes its decision rules, process variables, routing, API integration, and test scenarios. The `test-inputs` directory contains JSON start-variable examples for normal and exceptional paths.

---

# Lab 1 — End-to-End Loan Approval Automation

## Objective

Automate a loan application lifecycle using Camunda 8. The workflow validates applicant information, requests a simulated credit assessment, evaluates the application using a DMN decision table, and routes it to approval, rejection, or manual review.

## Workflow Design

1. The applicant submits synthetic applicant details, requested loan amount, income, existing debt, loan term, and employment information through the start form.
2. The `loan-validate-applicant` worker validates the supplied information and calculates financial ratios used by the decision process.
3. An exclusive gateway checks `validationPassed`. Invalid applications follow the invalid/incomplete application path.
4. The REST Connector calls the mock credit-assessment API.
5. The `loan_decision` DMN decision table classifies the application as **Approved**, **Rejected**, or **Manual Review**.
6. An exclusive gateway routes the decision:
   - **Approved:** the notification worker records the approved outcome.
   - **Rejected:** the notification worker records the rejected outcome.
   - **Manual Review:** a reviewer handles the application in Tasklist and records an approval or rejection.
7. A credit API error or timeout is handled through the configured boundary error route to manual review.
8. Operate provides visibility into process execution, variables, activity states, and incidents.

## DMN Decision Logic

The `loan_decision` decision table uses the **FIRST** hit policy and applies documented educational rules based on credit score, debt-to-income ratio, loan-to-income ratio, and employment duration.

| Decision | Educational rule summary |
|---|---|
| **Rejected** | The application falls below the configured credit threshold or exceeds the configured debt/loan affordability limits. |
| **Approved** | The application meets the configured credit, affordability, and employment criteria. |
| **Manual Review** | The application does not satisfy the complete approval criteria and does not meet a rejection rule. |

The exact input expressions, thresholds, and rule order are defined in [`lab1-loan-approval/docs/dmn-rules.md`](lab1-loan-approval/docs/dmn-rules.md).

## Worker and Mock Credit API

The Spring Boot application in `lab1-loan-approval/worker` provides the applicant validation and notification job workers and the mock credit-assessment endpoint.

| Item | Configuration |
|---|---|
| Worker application | `Lab1Application` |
| Worker job types | `loan-validate-applicant`, `loan-notify` |
| Mock API | `POST /api/credit/assess` |
| Base URL | `http://localhost:8081` |
| Worker/API port | `8081` |
| Camunda gRPC | `localhost:26500` |
| Camunda REST | `http://localhost:8080` |

The mock credit API returns deterministic synthetic assessment results for the supplied test identifiers. Identifiers beginning with `APP-FAIL` trigger a simulated HTTP 503 response. Identifiers beginning with `APP-SLOW` simulate a slow response for timeout handling.

## Main Test Scenarios

| Scenario | Test input | Expected workflow behaviour |
|---|---|---|
| Application meets approval criteria | `t1-approve.json` | Credit assessment and DMN evaluation route the application to approval. |
| Application fails eligibility | `t2-reject-low-score.json` | DMN routes the application to rejection. |
| Application requires manual review | `t3-manual-review.json` | A manual-review task is created in Tasklist. |
| Credit API returns an error | `t4-credit-api-error.json` | The connector error is routed to the manual-review path. |
| Credit API times out | `t4b-credit-api-timeout.json` | The timeout is handled through the configured credit API error route. |
| Worker failure and recovery | `t5-worker-failure.json` | The worker failure creates a retry/incident condition; after the simulated cause is corrected, the failed job can be retried in Operate. |
| Invalid applicant data | `t6-invalid-data.json` | Validation prevents the invalid application from proceeding to credit assessment. |

Detailed test inputs, variables, API requests, responses, and gateway routing are available in the Lab 1 `docs` and `test-inputs` directories.

---

# Lab 2 — End-to-End E-Commerce Order Processing and Fulfilment

## Objective

Automate an e-commerce order from validation through simulated payment and fulfilment. The workflow checks order details and inventory, calculates the order total, determines delivery priority using DMN, and handles payment approval, payment decline, and manual verification.

## Workflow Design

1. The customer submits a synthetic customer reference, product ID, quantity, delivery preference, and mock payment token.
2. The `order-validate` worker validates the order and obtains the simulated customer category.
3. An exclusive gateway checks whether the order is valid.
4. The `order-check-inventory` worker checks the simulated inventory.
5. An exclusive gateway prevents orders with insufficient stock from proceeding to payment and fulfilment.
6. The `order-calculate-total` worker calculates the order amount, including the configured shipping charge.
7. The `order_priority` DMN decision table determines the order priority and service-level target.
8. High-value orders are routed to a Tasklist verification task.
9. The REST Connector calls the mock payment service.
10. An exclusive gateway checks the payment outcome:
    - **Approved:** the fulfilment worker reserves stock and creates a simulated shipment.
    - **Declined:** the order follows the payment-declined path.
    - **Service failure:** the failed connector job can be inspected and retried after the simulated outage is corrected.

## DMN Decision Logic

The `order_priority` DMN decision table uses the **FIRST** hit policy. It evaluates order value, customer category, and delivery preference to produce an order priority and target service-level hours.

| Priority | Workflow purpose |
|---|---|
| **Urgent** | Highest configured fulfilment priority. |
| **High** | Elevated priority based on the configured order/customer/delivery conditions. |
| **Normal** | Standard fulfilment priority. |

The complete rule definitions and outputs are documented in [`lab2-order-processing/docs/dmn-rules.md`](lab2-order-processing/docs/dmn-rules.md).

## Worker and Mock Payment API

The Spring Boot application in `lab2-order-processing/worker` provides order validation, inventory checking, total calculation, fulfilment, and the mock payment endpoint.

| Item | Configuration |
|---|---|
| Worker application | `Lab2Application` |
| Worker job types | `order-validate`, `order-check-inventory`, `order-calculate-total`, `order-fulfil` |
| Mock API | `POST /api/payments/charge` |
| Base URL | `http://localhost:8082` |
| Worker/API port | `8082` |
| Successful mock token | `tok_test_ok` |
| Declined mock token | `tok_test_declined` |

Only mock payment tokens are used. The application rejects card-like values and does not require or process real payment credentials.

## Main Test Scenarios

| Scenario | Test input | Expected workflow behaviour |
|---|---|---|
| Stock available and payment succeeds | `t1-stock-and-payment-ok.json` | The order passes validation and inventory checks, payment is approved, and fulfilment completes. |
| Insufficient stock | `t2-insufficient-stock.json` | The order does not proceed to fulfilment. |
| Payment declined | `t3-payment-declined.json` | The payment gateway routes the order to the declined outcome. |
| High-value order | `t4-high-value-manual-review.json` | A manual verification task is created in Tasklist before payment. |
| Payment service outage | `t5-payment-outage.json` | The payment connector fails, an incident can be inspected in Operate, and the job can be retried after the outage is disabled. |
| Invalid order | `t6-invalid-order.json` | Validation prevents the invalid order from proceeding. |

## Payment Fault Simulation

The mock payment service includes an outage switch for demonstrating service failure and recovery.

```bash
# Enable the simulated payment-service outage
curl -X POST "http://localhost:8082/admin/outage/payment?enabled=true"

# Disable the outage after inspecting the incident
curl -X POST "http://localhost:8082/admin/outage/payment?enabled=false"

# Inspect the current outage-switch state
curl "http://localhost:8082/admin/outage"
```

After the service is restored, the failed job can be retried from Operate, where supported by the process state.

---

# Lab 3 — End-to-End Hospital Appointment and Patient Triage Management

## Objective

Develop a hospital appointment workflow that validates a synthetic appointment request, applies predefined educational triage rules, checks appointment availability through a mock scheduling API, and routes the request to booking or human staff review.

**Important scope:** The triage rules are an educational workflow simulation only. They are not clinical guidance, do not diagnose medical conditions, and must not be used to make real healthcare decisions.

## Workflow Design

1. A synthetic appointment request is submitted with a patient reference, department, preferred date, time of day, symptoms summary, severity, symptom duration, age group, and predefined urgency indicators.
2. The `triage-validate-registration` worker validates the registration fields, accepted values, synthetic patient reference format, and date.
3. The `triage_category` DMN decision table classifies the request.
4. An exclusive gateway routes the request by triage category:
   - **Routine:** proceeds to appointment availability checking.
   - **Priority Review:** creates a staff-review task in Tasklist. Staff can proceed with booking or refer the request elsewhere.
   - **Urgent Assessment:** creates an immediate human-assessment task in the simulated workflow.
5. For requests proceeding to booking, the REST Connector calls the mock scheduling API to retrieve suitable appointment slots.
6. An exclusive gateway checks the returned slot count:
   - **Slots available:** the booking worker books the first suitable slot.
   - **No slots available:** a staff-review task is created in Tasklist.
7. Operate is used to inspect process instances, variables, activity history, and scheduling-service incidents.

## Educational DMN Decision Logic

The `triage_category` decision table uses the **FIRST** hit policy and returns one of the following categories:

| Category | Workflow behaviour |
|---|---|
| **Routine** | Continues to the appointment availability search. |
| **Priority Review** | Routes the request to a staff-review task before booking or referral. |
| **Urgent Assessment** | Routes the request to a human assessment task in the simulated workflow. |

The rules use the predefined breathing and chest-pain indicators, severity, symptom duration, and age group. Full rule conditions are documented in [`lab3-patient-triage/docs/dmn-rules.md`](lab3-patient-triage/docs/dmn-rules.md).

## Worker and Mock Scheduling API

The Spring Boot application in `lab3-patient-triage/worker` provides registration validation, appointment booking, and the mock scheduling service.

| Item | Configuration |
|---|---|
| Worker application | `Lab3Application` |
| Worker job types | `triage-validate-registration`, `triage-book-appointment` |
| Mock API | `GET /api/scheduling/slots` |
| Base URL | `http://localhost:8083` |
| Worker/API port | `8083` |
| Patient reference format | `PT-SYN-nnnn` |
| Supported departments | General Medicine, Dermatology |
| Supported time preferences | Any, Morning, Afternoon |

The mock scheduling service returns synthetic appointment slots for the configured department and date range. The demonstration configuration includes a department with no available slots so that the no-slot staff-review route can be exercised.

## Main Test Scenarios

| Scenario | Test input | Expected workflow behaviour |
|---|---|---|
| Routine request with available slot | `t1-routine-with-slot.json` | The DMN returns Routine, slots are retrieved, and the booking worker books a suitable slot. |
| Priority staff review | `t2-priority-review.json` | A priority-review task is created in Tasklist before the request proceeds to booking or referral. |
| Urgent assessment | `t3-urgent-assessment.json` | The request is routed to the immediate human-assessment task. |
| No appointment slots | `t4-no-slots.json` | An empty slot response routes the request to staff review. |
| Scheduling API outage | `t5-scheduling-outage.json` | The connector failure is visible in Operate; after the outage is disabled, the failed job can be retried where supported. |
| Invalid registration | `t6-invalid-registration.json` | Registration validation prevents an invalid request from proceeding. |

## Scheduling Fault Simulation

The mock scheduling service includes an outage switch for demonstrating HTTP failure, incident inspection, and recovery.

```bash
# Enable the simulated scheduling-service outage
curl -X POST "http://localhost:8083/admin/outage/scheduling?enabled=true"

# Disable the outage after inspecting the incident
curl -X POST "http://localhost:8083/admin/outage/scheduling?enabled=false"

# Inspect the current outage-switch state
curl "http://localhost:8083/admin/outage"
```

After the service is restored, the failed connector job can be retried from Operate, where supported by the process state.

---

# Running the Project

## Prerequisites

- Java Development Kit (JDK) 17
- Apache Maven
- Camunda 8 Self-Managed / Camunda 8 Run version compatible with the Camunda Java client version configured in the worker projects
- Camunda Modeler
- A desktop environment capable of opening Camunda Tasklist and Operate in a browser

The three worker projects declare Camunda Spring Boot Starter **8.8.15** and Spring Boot **3.5.6** in their Maven configuration.

## Start Camunda 8

Start the local Camunda 8 environment and wait until its web applications are available.

| Component | Local address |
|---|---|
| Operate | `http://localhost:8080/operate` |
| Tasklist | `http://localhost:8080/tasklist` |
| Camunda REST API | `http://localhost:8080` |
| Zeebe gRPC | `localhost:26500` |

For a local demo environment using the default Camunda 8 Run credentials, the standard demo login is `demo` / `demo`.

## Build and Start a Lab Worker

Run one worker from its own directory. The worker also hosts that lab's mock REST API.

### Lab 1

```bash
cd lab1-loan-approval/worker
mvn clean package
mvn spring-boot:run
```

The mock credit API is available at `http://localhost:8081`.

### Lab 2

```bash
cd lab2-order-processing/worker
mvn clean package
mvn spring-boot:run
```

The mock payment API is available at `http://localhost:8082`.

### Lab 3

```bash
cd lab3-patient-triage/worker
mvn clean package
mvn spring-boot:run
```

The mock scheduling API is available at `http://localhost:8083`.

The worker applications use separate ports and can run alongside one another. Each worker connects to the local Camunda cluster using the gRPC address `http://localhost:26500` and REST address `http://localhost:8080`.

## Deploy BPMN, DMN, and Forms

Perform these steps separately for each lab:

1. Open the lab's `camunda-models` directory in Camunda Modeler.
2. Deploy the lab's DMN decision table and all associated `.form` files to the local Camunda 8 cluster.
3. Deploy the BPMN process model.
4. Confirm that the process definition is available in Tasklist.
5. Start a process instance using the corresponding start form or the JSON variables supplied in that lab's `test-inputs` directory.
6. Complete any assigned human tasks in Tasklist.
7. Inspect process execution, variables, activity history, and incidents in Operate.

Deploying the forms and decision requirements before the BPMN process ensures that the referenced form and decision resources are available to the process.

## Test Input Files

Each lab includes JSON files representing the required successful and exceptional scenarios.

- **Lab 1:** `lab1-loan-approval/test-inputs/`
- **Lab 2:** `lab2-order-processing/test-inputs/`
- **Lab 3:** `lab3-patient-triage/test-inputs/`

The expected routes and outcomes for each input are described in the corresponding `docs/test-cases.md` file.

---

# Process Monitoring and Failure Handling

The implementations demonstrate the distinction between business outcomes and technical failures.

| Condition | Meaning | Workflow handling |
|---|---|---|
| Invalid application, order, or registration | Input does not satisfy validation rules. | Validation result and gateway route the request to the relevant invalid/incomplete path. |
| Loan rejected | The DMN returns a business decision of Rejected. | The loan workflow records the rejection outcome. |
| Insufficient inventory | The inventory service reports that the requested quantity cannot be fulfilled. | The order does not proceed to payment and fulfilment. |
| Payment declined | The mock payment API responds successfully with a declined business status. | The payment gateway routes the order to the declined outcome. |
| No appointment slots | The scheduling API responds successfully but returns an empty slot list. | The hospital workflow creates a staff-review task. |
| REST API unavailable or timed out | A technical service failure prevents the connector from completing. | Retries are applied according to connector configuration; an incident can be inspected in Operate if retries are exhausted. |
| Worker job failure | A worker cannot complete its activated job. | Operate exposes the failed job/incident for diagnosis and retry after corrective action. |

## Incident Investigation and Recovery

The failure demonstrations use controlled mock-service or worker failure conditions. The recovery procedure is:

1. Open Operate and locate the affected process instance.
2. Inspect the incident and failed activity to identify the failing job or service call.
3. Correct the simulated cause, such as disabling the mock API outage or correcting the worker test condition.
4. Retry the failed job or incident from Operate when the process state and job configuration support retry.
5. Confirm that the process continues through the expected route and inspect the resulting variables and activity history.

The individual lab documentation contains the relevant fault-injection endpoint, test input, and expected recovery path.

---

# Process Variables and Detailed Documentation

Each lab has a dedicated process-variable dictionary and routing guide.

| Lab | Variable dictionary | Routing | DMN rules | REST examples | Test cases |
|---|---|---|---|---|---|
| Lab 1 — Loan Approval | [variables.md](lab1-loan-approval/docs/variables.md) | [gateway-routing.md](lab1-loan-approval/docs/gateway-routing.md) | [dmn-rules.md](lab1-loan-approval/docs/dmn-rules.md) | [api-examples.md](lab1-loan-approval/docs/api-examples.md) | [test-cases.md](lab1-loan-approval/docs/test-cases.md) |
| Lab 2 — Order Processing | [variables.md](lab2-order-processing/docs/variables.md) | [gateway-routing.md](lab2-order-processing/docs/gateway-routing.md) | [dmn-rules.md](lab2-order-processing/docs/dmn-rules.md) | [api-examples.md](lab2-order-processing/docs/api-examples.md) | [test-cases.md](lab2-order-processing/docs/test-cases.md) |
| Lab 3 — Hospital Triage | [variables.md](lab3-patient-triage/docs/variables.md) | [routing.md](lab3-patient-triage/docs/routing.md) | [dmn-rules.md](lab3-patient-triage/docs/dmn-rules.md) | [api-examples.md](lab3-patient-triage/docs/api-examples.md) | [test-cases.md](lab3-patient-triage/docs/test-cases.md) |

---

# Design Assumptions and Limitations

- All applicant, customer, order, payment, patient, symptom, and appointment information is synthetic.
- The credit-assessment, payment, and scheduling services are mock services intended for local workflow demonstrations.
- Loan decision thresholds and financial calculations are educational examples and are not financial advice or real lending policy.
- Order inventory and customer information are simulated in application memory; simulated inventory may reset when the worker application restarts.
- Payment processing uses mock tokens only. No real payment credentials are required or supported.
- Hospital triage categories are based on predefined educational rules only. They are not medically validated and are not intended for clinical use.
- Hospital patient references use synthetic identifiers. No real patient records or personally identifiable health information are required.
- Human review and staff authorisation are represented through Camunda Tasklist user tasks in the demonstration environment.
- The applications are configured for a local Camunda 8 environment. The mock APIs and workers must be running and reachable for their corresponding service tasks to complete.
- The outage switches are demonstration controls for local testing and are not intended to be exposed as production administration endpoints.

## Assignment Deliverables Represented in This Repository

- BPMN process models for all three workflow implementations.
- DMN decision tables and associated form resources.
- Java/Spring Boot external job worker source code.
- Mock REST API implementations and REST Connector examples.
- Process-variable dictionaries and gateway-routing explanations.
- Synthetic JSON test inputs covering successful, rejected, manual-review, and failure scenarios.
- Lab-specific documentation describing the workflow design, assumptions, and expected test behaviour.

## Conclusion

This project demonstrates end-to-end workflow automation with Camunda 8 across loan approval, e-commerce order processing, and hospital appointment management. The three implementations combine BPMN orchestration, DMN-based decisions, Java/Spring Boot workers, REST service integration, process-variable exchange, human task management, and incident monitoring through Operate. Together, they illustrate how business processes can coordinate automated services and human decisions while providing explicit handling for validation outcomes, service exceptions, and recovery paths.

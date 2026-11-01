# Official technical references

The implementation is pinned to Camunda 8.10 and the Spring Boot 3 integration. Check the documentation matching the version of your SaaS cluster if you use a different cluster version.

- [Camunda Spring Boot Starter — getting started and version compatibility](https://docs.camunda.io/docs/apis-tools/camunda-spring-boot-starter/getting-started/)
- [Camunda Spring Boot Starter — SaaS configuration, job workers, and job completion](https://docs.camunda.io/docs/apis-tools/camunda-spring-boot-starter/configuration/)
- [Camunda 8.10 — business rule tasks and DMN result variables](https://docs.camunda.io/docs/components/modeler/bpmn/business-rule-tasks/)
- [Camunda 8.10 — REST Connector](https://docs.camunda.io/docs/components/connectors/protocol/rest/)
- [Camunda 8 SaaS — manage API clients and OAuth access tokens](https://docs.camunda.io/docs/components/saas/clusters/manage-api-clients/)
- [Orchestration Cluster REST API — create process instance](https://docs.camunda.io/docs/next/apis-tools/orchestration-cluster-api-rest/specifications/create-process-instance/)

The SaaS client ID, secret, cluster ID, region, OAuth URL, token audience, and REST API address must be copied from the API client / cluster connection information shown by Camunda Hub. Never hardcode credentials in the BPMN, DMN, form, source code, or test fixtures.

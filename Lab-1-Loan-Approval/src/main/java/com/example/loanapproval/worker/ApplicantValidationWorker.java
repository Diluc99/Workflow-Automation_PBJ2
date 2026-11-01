package com.example.loanapproval.worker;

import com.example.loanapproval.service.ApplicantValidationService;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "camunda.worker", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ApplicantValidationWorker {
    private static final Logger log = LoggerFactory.getLogger(ApplicantValidationWorker.class);
    private final ApplicantValidationService validationService;

    public ApplicantValidationWorker(ApplicantValidationService validationService) {
        this.validationService = validationService;
    }

    @JobWorker(type = "validate-applicant", fetchAllVariables = true)
    public Map<String, Object> validateApplicant(ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        String applicantId = String.valueOf(variables.getOrDefault("applicantId", "UNKNOWN"));

        // Test switch: intentionally fail this job so an incident can be inspected in Operate.
        if (Boolean.TRUE.equals(variables.get("simulateWorkerFailure"))) {
            log.error("Simulated worker failure for applicant {}", applicantId);
            throw new IllegalStateException("SIMULATED_WORKER_FAILURE: set simulateWorkerFailure=false, then retry this job in Operate.");
        }

        Map<String, Object> result = validationService.validate(variables);
        log.info("Applicant validation completed: applicantId={}, validationPassed={}",
                applicantId, result.get("validationPassed"));
        return result;
    }
}

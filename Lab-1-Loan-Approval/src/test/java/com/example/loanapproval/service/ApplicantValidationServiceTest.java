package com.example.loanapproval.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicantValidationServiceTest {
    private final ApplicantValidationService service = new ApplicantValidationService();

    @Test
    void validatesWellFormedApplicantAndCalculatesRatio() {
        Map<String, Object> result = service.validate(Map.of(
                "applicantId", "APP-1001",
                "applicantName", "Aarav Sharma",
                "age", 30,
                "monthlyIncome", 80000,
                "requestedLoanAmount", 800000
        ));
        assertThat(result.get("validationPassed")).isEqualTo(true);
        assertThat((BigDecimal) result.get("loanToIncomeRatio")).isEqualByComparingTo("10.0000");
    }

    @Test
    void rejectsMalformedApplicantIdWithoutThrowing() {
        Map<String, Object> result = service.validate(Map.of(
                "applicantId", "INVALID",
                "applicantName", "Aarav Sharma",
                "age", 30,
                "monthlyIncome", 80000,
                "requestedLoanAmount", 800000
        ));
        assertThat(result.get("validationPassed")).isEqualTo(false);
        assertThat(result.get("validationMessage").toString()).contains("APP-1234");
    }

    @Test
    void leavesLowButPositiveIncomeForDmnEligibilityRules() {
        Map<String, Object> result = service.validate(Map.of(
                "applicantId", "APP-1002",
                "applicantName", "Mira Rao",
                "age", 29,
                "monthlyIncome", 12000,
                "requestedLoanAmount", 100000
        ));
        assertThat(result.get("validationPassed")).isEqualTo(true);
    }
}

package com.example.loanapproval.service;

import com.example.loanapproval.dto.CreditAssessmentRequest;
import com.example.loanapproval.dto.CreditAssessmentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreditAssessmentServiceTest {
    private final CreditAssessmentService service = new CreditAssessmentService();

    @Test
    void returnsSyntheticCreditAssessment() {
        CreditAssessmentResponse response = service.assess(new CreditAssessmentRequest(
                "APP-1001", new BigDecimal("80000"), new BigDecimal("800000"), 760, false));
        assertThat(response.creditScore()).isEqualTo(760);
        assertThat(response.riskBand()).isEqualTo("LOW");
        assertThat(response.assessmentStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void canSimulateCreditServiceOutage() {
        assertThatThrownBy(() -> service.assess(new CreditAssessmentRequest(
                "APP-1001", new BigDecimal("80000"), new BigDecimal("800000"), 760, true)))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("SIMULATED_CREDIT_API_FAILURE");
    }
}

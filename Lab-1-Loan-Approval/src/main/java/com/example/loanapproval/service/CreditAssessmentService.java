package com.example.loanapproval.service;

import com.example.loanapproval.dto.CreditAssessmentRequest;
import com.example.loanapproval.dto.CreditAssessmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreditAssessmentService {
    public CreditAssessmentResponse assess(CreditAssessmentRequest request) {
        if (request == null || request.applicantId() == null || request.applicantId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "applicantId is required");
        }
        if (request.monthlyIncome() == null || request.monthlyIncome().signum() <= 0
                || request.requestedLoanAmount() == null || request.requestedLoanAmount().signum() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "monthlyIncome and requestedLoanAmount must be positive");
        }
        if (Boolean.TRUE.equals(request.simulateCreditApiFailure())) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "SIMULATED_CREDIT_API_FAILURE: disable simulateCreditApiFailure and retry the failed connector job.");
        }

        int score = request.simulatedCreditScore() == null ? 650 : request.simulatedCreditScore();
        if (score < 300 || score > 900) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "simulatedCreditScore must be between 300 and 900");
        }
        String riskBand = score >= 700 ? "LOW" : score >= 550 ? "MEDIUM" : "HIGH";
        String reference = "CR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new CreditAssessmentResponse(reference, request.applicantId(), score, riskBand,
                "COMPLETED", "Synthetic Credit Bureau Simulator", Instant.now());
    }
}

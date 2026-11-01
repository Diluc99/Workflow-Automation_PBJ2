package com.example.loanapproval.dto;

import java.math.BigDecimal;

public record CreditAssessmentRequest(
        String applicantId,
        BigDecimal monthlyIncome,
        BigDecimal requestedLoanAmount,
        Integer simulatedCreditScore,
        Boolean simulateCreditApiFailure
) {}

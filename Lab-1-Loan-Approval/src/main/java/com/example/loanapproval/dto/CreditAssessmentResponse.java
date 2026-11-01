package com.example.loanapproval.dto;

import java.time.Instant;

public record CreditAssessmentResponse(
        String assessmentReference,
        String applicantId,
        int creditScore,
        String riskBand,
        String assessmentStatus,
        String provider,
        Instant assessedAt
) {}

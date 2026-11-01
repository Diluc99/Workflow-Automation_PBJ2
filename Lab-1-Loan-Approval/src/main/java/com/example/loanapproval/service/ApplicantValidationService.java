package com.example.loanapproval.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ApplicantValidationService {
    private static final Pattern APPLICANT_ID_PATTERN = Pattern.compile("APP-[0-9]{4}");

    /**
     * Performs structural validation only. Creditworthiness rules belong in the DMN table.
     * In particular, an income below INR 15,000 is allowed through this worker so that
     * the DMN can classify it as REJECTED according to the educational policy.
     */
    public Map<String, Object> validate(Map<String, Object> variables) {
        List<String> errors = new ArrayList<>();

        String applicantId = asString(variables.get("applicantId"));
        String applicantName = asString(variables.get("applicantName"));
        BigDecimal age = asDecimal(variables.get("age"));
        BigDecimal monthlyIncome = asDecimal(variables.get("monthlyIncome"));
        BigDecimal requestedLoanAmount = asDecimal(variables.get("requestedLoanAmount"));

        if (applicantId == null || !APPLICANT_ID_PATTERN.matcher(applicantId).matches()) {
            errors.add("applicantId must match APP-1234 format");
        }
        if (applicantName == null || applicantName.isBlank()) {
            errors.add("applicantName is required");
        }
        if (age == null || age.compareTo(BigDecimal.valueOf(18)) < 0 || age.compareTo(BigDecimal.valueOf(70)) > 0) {
            errors.add("age must be between 18 and 70 inclusive");
        }
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("monthlyIncome must be greater than zero");
        }
        if (requestedLoanAmount == null || requestedLoanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("requestedLoanAmount must be greater than zero");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        boolean passed = errors.isEmpty();
        result.put("validationPassed", passed);
        result.put("validationMessage", passed ? "Applicant details passed structural validation" : String.join("; ", errors));
        result.put("validationErrors", errors);
        if (passed) {
            BigDecimal ratio = requestedLoanAmount.divide(monthlyIncome, 4, RoundingMode.HALF_UP);
            result.put("loanToIncomeRatio", ratio);
        }
        return result;
    }

    private static String asString(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private static BigDecimal asDecimal(Object value) {
        if (value == null) return null;
        if (value instanceof BigDecimal bd) return bd;
        if (value instanceof Number number) return new BigDecimal(number.toString());
        try {
            return new BigDecimal(value.toString().trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

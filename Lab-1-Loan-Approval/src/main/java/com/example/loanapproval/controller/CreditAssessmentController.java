package com.example.loanapproval.controller;

import com.example.loanapproval.dto.CreditAssessmentRequest;
import com.example.loanapproval.dto.CreditAssessmentResponse;
import com.example.loanapproval.service.CreditAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CreditAssessmentController {
    private final CreditAssessmentService service;

    public CreditAssessmentController(CreditAssessmentService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "synthetic-credit-assessment-api");
    }

    @PostMapping("/credit-assessments")
    public ResponseEntity<CreditAssessmentResponse> assess(@RequestBody CreditAssessmentRequest request) {
        return ResponseEntity.ok(service.assess(request));
    }
}

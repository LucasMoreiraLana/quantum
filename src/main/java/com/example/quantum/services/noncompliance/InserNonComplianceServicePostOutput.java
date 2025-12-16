package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.NonCompliance;

public record InserNonComplianceServicePostOutput(
        NonCompliance nonCompliance,
        String createdByName
) {}

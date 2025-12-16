package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.NonCompliance;

import java.util.Optional;

public record GetNonComplianceServiceGetOutput(
        NonCompliance nonCompliance,
        Optional<String> createdByName
) {}

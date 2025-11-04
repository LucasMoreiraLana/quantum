package com.example.quantum.controllers.warning.updatewarning;

import com.example.quantum.domain.Warning;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record UpdateWarningPutRequest(
        UUID warningId,
        String warningTitle,
        String description,
        UUID processId,
        UUID documentId,
        Sector sector,
        int probability,
        int impact,
        int level,
        Warning.Avaliation avaliation,
        boolean active
) {
}

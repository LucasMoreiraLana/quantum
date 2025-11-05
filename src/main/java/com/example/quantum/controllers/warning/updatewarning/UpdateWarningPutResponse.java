package com.example.quantum.controllers.warning.updatewarning;

import com.example.quantum.domain.Warning;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record UpdateWarningPutResponse(
        UUID warningId,
        String warningTitle,
        UUID createdBy,
        String description,
        UUID processId,
        UUID documentId,
        Sector sector,
        int probability,
        int impact,
        int level,
        Warning.Avaliation avaliation,
        boolean active,
        int newProbability,
        int newImpact,
        int newLevel,
        Warning.Avaliation newAvaliation,
        int actions
) {
}

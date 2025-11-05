package com.example.quantum.controllers.warning.updatewarning;

import com.example.quantum.domain.Warning;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record UpdateWarningPutRequest(
        String warningTitle,
        String description,
        UUID processId,
        Sector sector,
        int probability,
        int impact,
        int level,
        Warning.Avaliation avaliation,
        boolean active,
        UUID documentId,
        int newProbability,
        int newImpact,
        int newLevel,
        Warning.Avaliation newAvaliation,
        int actions
) {}

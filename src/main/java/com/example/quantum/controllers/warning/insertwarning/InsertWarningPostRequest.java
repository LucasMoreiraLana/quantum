package com.example.quantum.controllers.warning.insertwarning;

import com.example.quantum.domain.Document;
import com.example.quantum.domain.Process;
import com.example.quantum.domain.Warning;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record InsertWarningPostRequest(
        String warningTitle,
        UUID createdBy,
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

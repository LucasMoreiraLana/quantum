package com.example.quantum.services.warning;

import com.example.quantum.domain.Document;
import com.example.quantum.domain.Process;
import com.example.quantum.domain.Warning;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record InsertWarningPostInput(
        UUID warningId,
        String warningTitle,
        UUID createdBy,
        String description,
        Process process,
        Sector sector,
        int probability,
        int impact,
        int level,
        Warning.Avaliation avaliation,
        boolean active,
        Document document,
        int newProbability,
        int newImpact,
        int newLevel,
        Warning.Avaliation newAvaliation,
        int actions
) {

    public Warning toDomain(){
        return new Warning(
                UUID.randomUUID(),
                warningTitle,
                createdBy,
                description,
                process,
                sector,
                probability,
                impact,
                level,
                avaliation,
                active,
                document,
                newProbability,
                newImpact,
                newLevel,
                newAvaliation,
                actions
        );
    }
}

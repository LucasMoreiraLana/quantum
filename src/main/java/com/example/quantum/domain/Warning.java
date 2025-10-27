package com.example.quantum.domain;

import com.example.quantum.enums.Sector;

import java.util.UUID;

public record Warning(

    UUID warningId,
    String warningTitle,
    UUID createdBy,
    String description,
    UUID processId,
    Sector sector,
    int probability,
    int impact,
    int level,
    Avaliation avaliation,
    boolean active,
    UUID documentId,
    int newProbability,
    int newImpact,
    int newLevel,
   Avaliation newAvaliation,
    int actions

){
    public enum Avaliation{
        RISCO_ALTO,
        RISCO_MEDIO
    }
}

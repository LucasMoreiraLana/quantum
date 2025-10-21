package com.example.quantum.repositories.identifywarning;

import com.example.quantum.domain.Warning;

public class WarningEntityMapper {

    public static Warning toWarning(WarningEntity entity){
        return new Warning(
                entity.getWarningId(),
                entity.getDescription(),
                entity.getProcess(),
                entity.getSector(),
                entity.getProbability(),
                entity.getImpact(),
                entity.getLevel(),
                entity.getAvaliation(),
                entity.isActive(),
                entity.getDocument(),
                entity.getNewProbability(),
                entity.getNewImpact(),
                entity.getNewLevel(),
                entity.getNewAvaliation(),
                entity.getActions()
        );
    }
}

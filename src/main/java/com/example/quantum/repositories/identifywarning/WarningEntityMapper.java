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

    public static WarningEntity toEntity(Warning warning){
        WarningEntity entity = new WarningEntity();
        entity.setWarningId(warning.warningId());
        entity.setDescription(warning.description());
        entity.setProcess(warning.process());
        entity.setSector(warning.sector());
        entity.setProbability(warning.probability());
        entity.setImpact(warning.impact());
        entity.setLevel(warning.level());
        entity.setAvaliation(warning.avaliation());
        entity.setActive(warning.active());
        entity.setDocument(warning.document());
        entity.setNewProbability(warning.newProbability());
        entity.setNewImpact(warning.newImpact());
        entity.setNewLevel(warning.newLevel());
        entity.setNewAvaliation(warning.newAvaliation());
        entity.setActions(warning.actions());
        return entity;
    }
}

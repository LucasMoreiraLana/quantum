package com.example.quantum.controllers.warning.getwarning;

import com.example.quantum.domain.Warning;
import com.example.quantum.repositories.warning.WarningEntity;

public class GetWarningByIdGetMapper {

    public static GetWarningByIdGetResponse toWarningByIdResponse(Warning warning){
        return new GetWarningByIdGetResponse(
                warning.warningId(),
                warning.warningTitle(),
                warning.createdBy(),
                warning.description(),
                warning.processId(),
                warning.sector(),
                warning.probability(),
                warning.impact(),
                warning.level(),
                warning.avaliation(),
                warning.active(),
                warning.documentId(),
                warning.newProbability(),
                warning.newImpact(),
                warning.newLevel(),
                warning.newAvaliation(),
                warning.actions()
        );
    }

    public static Warning toDomain(WarningEntity entity){
        if(entity == null) return null;

        return new Warning(
                entity.getWarningId(),
                entity.getWarningTitle(),
                entity.getCreatedBy(),
                entity.getDescription(),
                entity.getProcessId(),
                entity.getSector(),
                entity.getProbability(),
                entity.getImpact(),
                entity.getLevel(),
                entity.getAvaliation(),
                entity.isActive(),
                entity.getDocumentId(),
                entity.getNewProbability(),
                entity.getNewImpact(),
                entity.getNewLevel(),
                entity.getNewAvaliation(),
                entity.getActions()
        );
    }
}

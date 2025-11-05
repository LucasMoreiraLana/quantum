package com.example.quantum.controllers.warning.updatewarning;

import com.example.quantum.domain.Warning;
import com.example.quantum.services.warning.UpdateWarningPutInput;

import java.util.UUID;

public class UpdateWarningPutMapper {

    public static UpdateWarningPutInput toWarningInput(UUID warningId, UpdateWarningPutRequest request){
        return new UpdateWarningPutInput(
                warningId,
                request.warningTitle(),
                request.description(),
                request.processId(),
                request.sector(),
                request.probability(),
                request.impact(),
                request.level(),
                request.avaliation(),
                request.active(),
                request.documentId(),
                request.newProbability(),
                request.newImpact(),
                request.newLevel(),
                request.newAvaliation(),
                request.actions()
        );
    }

    public static UpdateWarningPutResponse toWarningResponse(Warning warning) {
        return new UpdateWarningPutResponse(
                warning.warningId(),
                warning.warningTitle(),
                warning.createdBy(),
                warning.description(),
                warning.processId(),
                warning.documentId(),
                warning.sector(),
                warning.probability(),
                warning.impact(),
                warning.level(),
                warning.avaliation(),
                warning.active(),
                warning.newProbability(),
                warning.newImpact(),
                warning.newLevel(),
                warning.newAvaliation(),
                warning.actions()
        );
    }
}

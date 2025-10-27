package com.example.quantum.controllers.warning.insertwarning;

import com.example.quantum.domain.Warning;
import com.example.quantum.services.warning.InsertWarningPostInput;

import java.util.UUID;

public class InsertWarningPostMapper {

    public static InsertWarningPostInput toInput(InsertWarningPostRequest request){
        return new InsertWarningPostInput(
                UUID.randomUUID(),
                request.warningTitle(),
                request.createdBy(),
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

    public static InsertWarningPostResponse toResponse(Warning warning){
        return new InsertWarningPostResponse(
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
}

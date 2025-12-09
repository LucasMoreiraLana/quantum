package com.example.quantum.controllers.process.updateprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.services.process.UpdateProcessPutInput;

import java.util.UUID;

public class UpdateProcessPutMapper {

    public static UpdateProcessPutInput toInput(UUID processId, UpdateProcessPutRequest request){
        return new UpdateProcessPutInput(
                processId,
                null,
                request.nameProcess(),
                request.dateApproval(),
                request.dateConclusion(),
                request.sector(),
                request.cyclePDCA()
        );
    }

    public static UpdateProcessPutResponse toResponse(Process process){
        return new UpdateProcessPutResponse(
                process.createdBy(),
                process.nameProcess(),
                process.dateApproval(),
                process.dateConclusion(),
                process.sector(),
                process.cyclePDCA()
        );
    }
}

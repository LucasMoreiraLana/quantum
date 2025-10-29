package com.example.quantum.controllers.process.getprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntity;

public class GetProcessByIdGetMapper {

    public static GetProcessByIdGetResponse toResponse(Process process) {
        return new GetProcessByIdGetResponse(
                process.processId(),
                process.createdBy(),
                process.nameProcess(),
                process.dateApproval(),
                process.dateConclusion(),
                process.sector(),
                process.cyclePDCA()
        );
    }

    // Converte de Entity (banco) para Domain (negócio)
    public static Process toDomain(ProcessEntity entity) {
        if (entity == null) return null;

        return new Process(
                entity.getProcessId(),
                entity.getCreatedBy(),
                entity.getNameProcess(),
                entity.getDateApproval(),
                entity.getDateConclusion(),
                entity.getSector(),
                entity.getCyclePDCA()
        );
    }

}

package com.example.quantum.controllers.process.getbyidprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntity;

public class GetByIdProcessGetMapper {

    public static GetByIdProcessGetResponse toResponse(Process process) {
        return new GetByIdProcessGetResponse(
                process.idProcess(),
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
                entity.getIdProcess(),
                entity.getCreatedBy(),
                entity.getNameProcess(),
                entity.getDateApproval(),
                entity.getDateConclusion(),
                entity.getSector(),
                entity.getCyclePDCA()
        );
    }

    // Converte de Domain (negócio) para Entity (banco)
    public static ProcessEntity toEntity(Process domain) {
        if (domain == null) return null;

        ProcessEntity entity = new ProcessEntity();
        entity.setIdProcess(domain.idProcess());
        entity.setCreatedBy(domain.createdBy());
        entity.setNameProcess(domain.nameProcess());
        entity.setDateApproval(domain.dateApproval());
        entity.setDateConclusion(domain.dateConclusion());
        entity.setSector(domain.sector());
        entity.setCyclePDCA(domain.cyclePDCA());

        return entity;
    }
}

package com.example.quantum.repositories.process;

import com.example.quantum.domain.Process;

public class ProcessEntityMapper {

    public static Process toProcess(ProcessEntity entity){

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

    public static ProcessEntity toEntity(Process process){
        ProcessEntity entity = new ProcessEntity();
        entity.setProcessId(process.processId());
        entity.setCreatedBy(process.createdBy());
        entity.setNameProcess(process.nameProcess());
        entity.setDateApproval(process.dateApproval());
        entity.setDateConclusion(process.dateConclusion());
        entity.setSector(process.sector());
        entity.setCyclePDCA(process.cyclePDCA());
        return entity;
    }
}

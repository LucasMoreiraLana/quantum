package com.example.quantum.mappers;

import org.springframework.stereotype.Component;

import com.example.quantum.dtos.request.processes.ProcessCreateRequest;
import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntity;

@Component
public class ProcessMapper {
    public ProcessEntity toEntity(ProcessCreateRequest request) {
        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setNameProcess(request.nameProcess());
        processEntity.setDateApproval(request.dateApproval());
        processEntity.setDateConclusion(request.dateConclusion());
        processEntity.setSector(request.sector());
        processEntity.setCycle(request.cyclePDCA());
        return processEntity;
    

    }

    public Process toResponse(ProcessEntity processEntity) {
        return new Process(
                processEntity.getIdProcess(),
                processEntity.getNameProcess(),
                processEntity.getDateApproval(),
                processEntity.getDateConclusion(),
                processEntity.getSector(),
                processEntity.getCycle()
        );
    }

    public void updateEntityFromDTO(ProcessCreateRequest request, ProcessEntity processEntity) {
        if (request.nameProcess() != null) {
            processEntity.setNameProcess(request.nameProcess());
        }
        if (request.dateApproval() != null) {
            processEntity.setDateApproval(request.dateApproval());
        }
        if (request.dateConclusion() != null) {
            processEntity.setDateConclusion(request.dateConclusion());
        }
        if (request.sector() != null) {
            processEntity.setSector(request.sector());
        }
        if (request.cyclePDCA() != null) {
            processEntity.setCycle(request.cyclePDCA());
        }
    }
}

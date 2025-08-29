package com.example.quantum.mappers;

import org.springframework.stereotype.Component;

import com.example.quantum.dtos.request.processes.ProcessCreateRequest;
import com.example.quantum.dtos.response.processes.ProcessResponse;
import com.example.quantum.models.Process;

@Component
public class ProcessMapper {
    public Process toEntity(ProcessCreateRequest request) {
        Process process = new Process();
        process.setNameProcess(request.nameProcess());
        process.setDateApproval(request.dateApproval());
        process.setDateConclusion(request.dateConclusion());
        process.setSector(request.sector());
        process.setCyclePDCA(request.cyclePDCA());
        return process;
    

    }

    public ProcessResponse toResponse(Process process) {
        return new ProcessResponse(
                process.getIdProcess(),
                process.getNameProcess(),
                process.getDateApproval(),
                process.getDateConclusion(),
                process.getSector(),
                process.getCyclePDCA()
        );
    }

    public void updateEntityFromDTO(ProcessCreateRequest request, Process process) {
        if (request.nameProcess() != null) {
            process.setNameProcess(request.nameProcess());
        }
        if (request.dateApproval() != null) {
            process.setDateApproval(request.dateApproval());
        }
        if (request.dateConclusion() != null) {
            process.setDateConclusion(request.dateConclusion());
        }
        if (request.sector() != null) {
            process.setSector(request.sector());
        }
        if (request.cyclePDCA() != null) {
            process.setCyclePDCA(request.cyclePDCA());
        }
    }
}

package com.example.quantum.controllers.process.getprocess;


import com.example.quantum.domain.Process;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllProcessGetMapper {

    public static GetAllProcessGetResponse toResponse(Process process){
        return new GetAllProcessGetResponse(
                process.processId(),
                process.createdBy(),
                process.nameProcess(),
                process.dateApproval(),
                process.dateConclusion(),
                process.sector(),
                process.cyclePDCA()
        );
    }

    public static List<GetAllProcessGetResponse> toResponseList(List<Process> processes){
        return processes.stream()
                .map(GetAllProcessGetMapper::toResponse)
                .collect(Collectors.toList());
    }
}

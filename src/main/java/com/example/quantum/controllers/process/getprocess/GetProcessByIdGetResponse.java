package com.example.quantum.controllers.process.getprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat; // Não esqueça deste import!

import java.time.LocalDate;
import java.util.UUID;

public record GetProcessByIdGetResponse(
        UUID processId,
        UUID createdBy,
        String nameProcess,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateApproval,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateConclusion,
        Sector sector,
        Process.Cycle cyclePDCA
){}
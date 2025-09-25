package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateProcessPutInput(
        UUID processId,
        UUID createdBy,
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        Process.Cycle cyclePDCA
){}

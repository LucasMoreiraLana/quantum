package com.example.quantum.controllers.process.updateprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateProcessPutRequest(
        @NotNull
        UUID processId,
        @NotNull
        UUID createdBy,
        @NotNull
        String nameProcess,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateApproval,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateConclusion,
        @NotNull
        Sector sector,
        @NotNull
        Process.Cycle cyclePDCA
) {}

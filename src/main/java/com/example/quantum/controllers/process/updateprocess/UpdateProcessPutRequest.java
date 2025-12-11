package com.example.quantum.controllers.process.updateprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UpdateProcessPutRequest(
        // REMOVIDO: processId e createdBy (Eles são injetados/passados pela URL)

        @NotBlank String nameProcess,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull LocalDate dateApproval,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull LocalDate dateConclusion,
        @NotNull Sector sector,
        @NotNull Process.Cycle cyclePDCA
) {}


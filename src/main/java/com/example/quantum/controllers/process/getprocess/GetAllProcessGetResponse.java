package com.example.quantum.controllers.process.getprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record GetAllProcessGetResponse(
    UUID processId,
    UUID createdBy,
    String nameProcess,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateApproval,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateConclusion,
    Sector sector,
    Process.Cycle cyclePDCA
){}

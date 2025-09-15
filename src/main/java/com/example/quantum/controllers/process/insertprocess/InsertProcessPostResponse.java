package com.example.quantum.controllers.process.insertprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertProcessPostResponse(
        UUID createdBy,
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        Process.Cycle cycle
){}

package com.example.quantum.controllers.process.insertprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import java.time.LocalDate;


public record InsertProcessPostRequest(
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        Process.Cycle cyclePDCA
) {}
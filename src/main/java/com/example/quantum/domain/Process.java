package com.example.quantum.domain;
import java.time.LocalDate;
import java.util.UUID;

import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat;


public record Process(
        UUID processId,
        UUID createdBy,
        String nameProcess,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateApproval,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateConclusion,
        Sector sector,
        Cycle cyclePDCA
) {

    public enum Cycle {
        P,
        D,
        C,
        A
    }
    
}

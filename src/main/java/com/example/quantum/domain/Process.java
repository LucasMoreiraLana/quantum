package com.example.quantum.domain;
import java.time.LocalDate;
import java.util.UUID;

import com.example.quantum.enums.Sector;


public record Process(
        UUID idProcess,
        UUID createdBy,
        String nameProcess,
        LocalDate dateApproval,
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

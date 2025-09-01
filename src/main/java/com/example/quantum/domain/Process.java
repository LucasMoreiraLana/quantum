package com.example.quantum.domain;
import java.time.LocalDate;
import java.util.UUID;

import com.example.quantum.enums.Sector;


public record Process(
        UUID idProcess,
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        Cycle cycle
) {

    public enum Cycle {
        P,
        D,
        C,
        A
    }
    
}

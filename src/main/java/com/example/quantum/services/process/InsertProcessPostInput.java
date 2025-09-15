package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertProcessPostInput(

        UUID createdBy,
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        Process.Cycle cyclePDCA

) {

    public Process toDomain(){
        return new Process(
                UUID.randomUUID(),
                this.createdBy,
                this.nameProcess,
                this.dateApproval,
                this.dateConclusion,
                this.sector,
                this.cyclePDCA
        );
    }
}

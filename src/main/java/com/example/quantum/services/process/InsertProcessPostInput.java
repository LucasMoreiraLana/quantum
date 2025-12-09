package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import java.time.LocalDate;
import java.util.UUID;

// O Input AGORA deve incluir o createdBy, pois ele será injetado pelo Controller.
public record InsertProcessPostInput(
        UUID processId,
        UUID createdBy,
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        Process.Cycle cyclePDCA
) {
    // Adicionar um método de conversão (exemplo)
    public Process toDomain() {
        return new Process(
                this.processId,
                this.createdBy,
                this.nameProcess,
                this.dateApproval,
                this.dateConclusion,
                this.sector,
                this.cyclePDCA
        );
    }
}
package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.Document;
import com.example.quantum.domain.NonCompliance;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertNonCompliancePostInput(

        UUID createdBy,
        LocalDate dateOpening,
        UUID ProcessId,          // apenas o ID do processo
        Sector sector,
        Document.Origin origin,
        Priority priority,
        String customer,
        String description,
        boolean efficacy,
        LocalDate datePrevision

) {
    public NonCompliance toDomain() {
        return new NonCompliance(
                UUID.randomUUID(), // Sempre gera aqui
                this.createdBy,
                this.dateOpening,
                this.ProcessId,
                this.sector,
                this.origin,
                this.priority,
                this.customer,
                this.description,
                this.efficacy, // aqui pode vir do input ou true fixo
                this.datePrevision
        );
    }

}

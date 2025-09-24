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
        UUID idProcess,          // apenas o ID do processo
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
                null,             // id gerado pelo banco
                this.createdBy,
                this.dateOpening,
                null,             // process será resolvido no service
                this.sector,
                this.origin,
                this.priority,
                this.customer,
                this.description,             // sem Action por enquanto
                true,
                this.datePrevision
        );
    }
}

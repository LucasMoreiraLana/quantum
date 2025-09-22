package com.example.quantum.services.noncompliance;
import com.example.quantum.domain.Action;
import com.example.quantum.domain.Document;
import com.example.quantum.domain.NonCompliance;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;
import com.example.quantum.domain.Process;


import java.time.LocalDate;
import java.util.UUID;

public record InsertNonCompliancePostInput(

        UUID createdBy,
        LocalDate dateOpening,
        Process nameProcess,
        Sector sector,
        Document.Origin origin,
        Priority priority,
        String customer,
        Action action,
        String description,
        boolean efficacy,
        LocalDate datePrevision

){

    public NonCompliance toDomain(){
        return new NonCompliance(
                UUID.randomUUID(),
                this.createdBy,
                this.dateOpening,
                this.nameProcess,
                this.sector,
                this.origin,
                this.priority,
                this.customer,
                this.action,
                this.description,
                true,
                this.datePrevision

        );
    }
}

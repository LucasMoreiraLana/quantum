package com.example.quantum.services.noncompliance;
import com.example.quantum.domain.Action;
import com.example.quantum.domain.Document;
import com.example.quantum.domain.NonCompliance;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;



import java.time.LocalDate;
import java.util.UUID;

public record InsertNonCompliancePostInput(

        UUID createdBy,
        LocalDate dateOpening,
        UUID idProcess,
        Sector sector,
        Document.Origin origin,
        Priority priority,
        String customer,
        UUID action,
        String description,
        boolean efficacy,
        LocalDate datePrevision

){

    public NonCompliance toDomain(){
        return new NonCompliance(
                null,
                this.createdBy,
                this.dateOpening,
                null,
                this.sector,
                this.origin,
                this.priority,
                this.customer,
                null,
                this.description,
                true,
                this.datePrevision

        );
    }
}

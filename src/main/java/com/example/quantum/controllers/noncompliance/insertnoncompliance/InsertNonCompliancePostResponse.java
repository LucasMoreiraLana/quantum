package com.example.quantum.controllers.noncompliance.insertnoncompliance;

import com.example.quantum.domain.Document;
import com.example.quantum.domain.Process;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertNonCompliancePostResponse(
        UUID nonComplianceId,
        UUID createdBy,
        LocalDate dateOpening,
        Process processId,
        Sector sector,
        Document.Origin origin,
        Priority priority,
        String customer,
        String description,
        boolean efficacy,
        LocalDate datePrevision

) {
}

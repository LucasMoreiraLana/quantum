package com.example.quantum.controllers.noncompliance.updatenoncompliance;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateNonCompliancePutRequest(

        UUID nonComplianceId,
        UUID createdBy,
        LocalDate dateOpening,
        UUID processId,
        Sector sector,
        DocumentOrigin origin,
        Priority priority,
        String customer,
        String description,
        boolean efficacy,
        LocalDate datePrevision

){}

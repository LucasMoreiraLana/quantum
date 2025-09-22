package com.example.quantum.controllers.noncompliance.insertnoncompliance;

import com.example.quantum.domain.Action;
import com.example.quantum.domain.Document;
import com.example.quantum.domain.Process;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertNonCompliancePostRequest(

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

){}

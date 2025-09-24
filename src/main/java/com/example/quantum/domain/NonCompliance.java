package com.example.quantum.domain;

import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record NonCompliance(

        UUID idNonCompliance,
        UUID createdBy,
        LocalDate dateOpening,
        Process idProcess,
        Sector sector,
        Document.Origin origin,
        Priority priority,
        String customer,
        String description,
        boolean efficacy,
        LocalDate datePrevision

) {

}

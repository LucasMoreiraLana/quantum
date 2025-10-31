package com.example.quantum.controllers.warning.getwarning;

import com.example.quantum.enums.Sector;

import java.util.UUID;

public record GetAllWarningGetResponse(

        String warningTitle,
        UUID createdBy,
        String description,
        UUID documentId,
        UUID processId,
        Sector sector,
        boolean active

) {}

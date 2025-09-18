package com.example.quantum.controllers.document.getbyiddocument;

import com.example.quantum.domain.Document;
import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record GetByIdDocumentGetResponse(

        UUID idDocument,
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoRetencao,
        boolean active,
        Document.Type type,
        Document.Origin origin,
        Sector sector
) {}

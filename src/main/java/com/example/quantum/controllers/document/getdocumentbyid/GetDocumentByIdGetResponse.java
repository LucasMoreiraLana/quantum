package com.example.quantum.controllers.document.getdocumentbyid;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record GetDocumentByIdGetResponse(

        UUID documentId,
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoRetencao,
        boolean active,
        Document.Type type,
        Document.Origin origin,
        Sector sector
) {}

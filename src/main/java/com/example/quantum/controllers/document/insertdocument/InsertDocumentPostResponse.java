package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record InsertDocumentPostResponse(
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        Document.Type type,
        Document.Origin origin,
        Sector sector,
        boolean active
) {}

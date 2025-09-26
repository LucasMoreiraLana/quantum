package com.example.quantum.services.document;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record UpdateDocumentPutInput(
        UUID documentId,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        Document.Type type,
        Document.Origin origin,
        Sector sector
) {}


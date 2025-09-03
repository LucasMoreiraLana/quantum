package com.example.quantum.controllers.document;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;
import java.util.UUID;

public record GetDocumentGetResponse(
        UUID idDocument,
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        boolean active,
        Document.Type type,
        Document.Origin origin,
        Sector sector
) {}

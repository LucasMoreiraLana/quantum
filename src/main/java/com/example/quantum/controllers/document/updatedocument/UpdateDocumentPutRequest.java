package com.example.quantum.controllers.document.updatedocument;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record UpdateDocumentPutRequest(

        UUID documentId,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        boolean active,
        DocumentType type,
        DocumentOrigin origin,
        Sector sector

) {}

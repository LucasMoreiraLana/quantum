package com.example.quantum.controllers.document.getdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record GetDocumentByIdGetResponse(

        UUID documentId,
        UUID createdBy,
        String nameDocument,
        String content,

        // CORRIGIDO: Alinhamento ao nome do DTO de Update/Frontend
        int tempoDeRetencao,

        boolean active,
        DocumentType type,
        DocumentOrigin origin,
        Sector sector,

        // NOVO: Campo obrigatório para o Frontend
        String createdByName
) {}
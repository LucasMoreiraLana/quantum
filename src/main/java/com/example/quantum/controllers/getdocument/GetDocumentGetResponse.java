package com.example.quantum.controllers.getdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;

public record GetDocumentGetResponse(
        
        String nameDocument,
        String content,
        int tempoDeRetencao,
        boolean active,
        Document.Type type,
        Document.Origin origin,
        Sector sector
) {}

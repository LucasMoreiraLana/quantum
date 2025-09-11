package com.example.quantum.controllers.document.updatedocument;


import com.example.quantum.enums.Sector;
import com.example.quantum.domain.Document;

public record UpdateDocumentPutResponse(

        String nameDocument,
        String content,
        int tempoDeRetencao,
        Document.Type type,
        Document.Origin origin,
        Sector sector,
        boolean active

) {}

package com.example.quantum.controllers.document.updatedocument;


import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Sector;
import com.example.quantum.domain.Document;

public record UpdateDocumentPutResponse(

        String nameDocument,
        String content,
        int tempoDeRetencao,
        DocumentType type,
        DocumentOrigin origin,
        Sector sector,
        boolean active

) {}

package com.example.quantum.controllers.document.insertdocument;


import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record InsertDocumentPostRequest(
        String nameDocument,
        String content,
        int tempoRetencao,
        boolean active,
        Document type,
        Document origin,
        Sector sector
) {}

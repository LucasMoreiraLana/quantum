package com.example.quantum.services.document;

import com.example.quantum.domain.Document;

public record InsertDocumentServicePostOutput(
        Document document,
        String createdByName
) {}
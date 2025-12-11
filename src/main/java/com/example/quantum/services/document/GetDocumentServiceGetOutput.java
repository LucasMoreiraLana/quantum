package com.example.quantum.services.document;

import com.example.quantum.domain.Document;

import java.util.Optional;

public record GetDocumentServiceGetOutput(
        Document document,
        Optional<String> createdByName
) {}

package com.example.quantum.dtos.request.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;

public record DocumentUpdateRequest(
        String nameDocument,
        String content,
        Integer tempoDeRetencao,
        DocumentType type,
        Origin origin,
        Sector sector
) {}

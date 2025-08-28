package com.example.quantum.dtos.response.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import com.example.quantum.models.User;
import java.util.UUID;

public record DocumentResponse(
        UUID idDocument,
        User user,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        boolean active,
        DocumentType type,
        Origin origin,
        Sector sector
) {}

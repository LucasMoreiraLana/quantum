package com.example.quantum.domain;

import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat; // NOVO IMPORT!

import java.util.UUID;

public record Document(

        UUID documentId,
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        boolean active,

        // CORREÇÃO 1: Adicionar anotação para forçar Jackson a usar o nome da String
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        DocumentType type,

        // CORREÇÃO 2: Adicionar anotação para forçar Jackson a usar o nome da String
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        DocumentOrigin origin,

        @JsonFormat(shape = JsonFormat.Shape.STRING) // Boa prática, se Sector for Enum
        Sector sector
) {

}
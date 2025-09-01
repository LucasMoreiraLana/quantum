package com.example.quantum.domain;


import com.example.quantum.enums.Sector;

import java.util.UUID;

public record Document(
        UUID idDocument,
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        boolean active,
        Type type,
        Origin origin,
        Sector sector
) {


    public enum Type {
        REGISTRO,
        PROCEDIMENTO,
        INSTRUCAO_TECNICA,
        FORMULARIO,
        REGULAMENTO,
        SISTEMA_INFORMATIZADO
    }

    public enum Origin {
        INTERNO,
        EXTERNO
    }
}



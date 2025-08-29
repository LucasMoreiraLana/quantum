package com.example.quantum.dtos.request.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DocumentUpdateRequest(
        @NotNull(message = "Você precisa informar o usuário associado ao documento!")
        String nameDocument,
        @NotNull(message = "Você precisa informar o conteúdo do documento!")
        @Size(max = 5000)
        String content,
        @NotNull(message = "O tempo de retenção deve ser maior que zero!")
        Integer tempoDeRetencao,
        @NotNull(message = "O tipo do documento precisa ser informado!")
        DocumentType type,
        @NotNull(message = "A origem do documento precisa ser informada!")
        Origin origin,
        @NotNull(message = "O setor do documento precisa ser informado!")
        Sector sector
) {}

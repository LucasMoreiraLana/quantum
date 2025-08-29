package com.example.quantum.dtos.request.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import com.example.quantum.models.User;
import jakarta.validation.constraints.*;

public record DocumentCreateRequest(
        @NotNull(message = "Você precisa informar o usuário associado ao documento!")
        User user,
        @NotBlank(message = "O nome do documento não pode ser vazio!")
        String nameDocument,
        @NotBlank(message = "A descrição do documento não pode ser vazia!")
        @Size(max = 5000)
        String content,
        @Positive(message = "O tempo de retenção deve ser maior que zero!")
        int tempoDeRetencao,
        @NotNull(message = "O tipo do documento precisa ser informado!")
        DocumentType type,
        @NotNull(message = "A origem do documento precisa ser informada!")
        Origin origin,
        @NotNull(message = "O setor do documento precisa ser informado!")
        Sector sector
) {}

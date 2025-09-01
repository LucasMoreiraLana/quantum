package com.example.quantum.controllers.documents;


import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;
import com.example.quantum.repositories.user.UserEntity;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record InsertDocumentPostRequest(
        @NotNull(message = "Você precisa informar o usuário associado ao documento!")
        UUID createdBy,
        @NotBlank(message = "O nome do documento não pode ser vazio!")
        String nameDocument,
        @NotBlank(message = "A descrição do documento não pode ser vazia!")
        @Size(max = 5000)
        String content,
        @Positive(message = "O tempo de retenção deve ser maior que zero!")
        int tempoDeRetencao,
        @NotNull(message = "O tipo do documento precisa ser informado!")
        Document.Type type,
        @NotNull(message = "A origem do documento precisa ser informada!")
        Document.Origin origin,
        @NotNull(message = "O setor do documento precisa ser informado!")
        Sector sector
) {}

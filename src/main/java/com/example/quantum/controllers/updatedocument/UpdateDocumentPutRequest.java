package com.example.quantum.controllers.updatedocument;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDocumentPutRequest(
        @NotNull(message = "Você precisa informar o usuário associado ao documento!")
        String nameDocument,
        @NotNull(message = "Você precisa informar o conteúdo do documento!")
        @Size(max = 5000)
        String content,
        @NotNull(message = "O tempo de retenção deve ser maior que zero!")
        Integer tempoDeRetencao,
        @NotNull(message = "O tipo do documento precisa ser informado!")
        Document.Type type,
        @NotNull(message = "A origem do documento precisa ser informada!")
        Document.Origin origin,
        @NotNull(message = "O setor do documento precisa ser informado!")
        Sector sector
) {
}

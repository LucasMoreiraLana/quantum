package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Sector;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

public record InsertDocumentPostRequest(

        @NotBlank String nameDocument,
        @NotBlank String content,
        @NotNull @Min(1) int tempoDeRetencao, // Use @Min(1) para garantir um valor válido

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull DocumentType type,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull DocumentOrigin origin,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull Sector sector
) {}
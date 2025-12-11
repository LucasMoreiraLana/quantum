package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.domain.Document;

public class InsertDocumentPostMapper {

    // Domain → Response (AGORA ACEITA O NOME DO CRIADOR)
    public static InsertDocumentPostResponse toDocumentResponse(
            Document document,
            String createdByName // NOVO ARGUMENTO
    ) {
        return new InsertDocumentPostResponse(
                document.createdBy(),
                document.nameDocument(),
                document.content(),
                document.tempoDeRetencao(),
                document.type(),
                document.origin(),
                document.sector(),
                document.active(),
                createdByName // Mapeia o nome
        );
    }
}
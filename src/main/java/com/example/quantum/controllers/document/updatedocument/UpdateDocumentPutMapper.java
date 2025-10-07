package com.example.quantum.controllers.document.updatedocument;

import java.util.UUID;

import com.example.quantum.domain.Document;
import com.example.quantum.services.document.UpdateDocumentPutInput;

public class UpdateDocumentPutMapper {

    // Request → Input
    public static UpdateDocumentPutInput toInput(UUID documentId, UpdateDocumentPutRequest request) {
        return new UpdateDocumentPutInput(
                documentId,
                request.nameDocument(),
                request.content(),
                request.tempoDeRetencao(),
                request.type(),
                request.origin(),
                request.sector()
        );
    }

    // Domain → Response
    public static UpdateDocumentPutResponse toResponse(Document document) {
        return new UpdateDocumentPutResponse(
                document.nameDocument(),
                document.content(),
                document.tempoDeRetencao(),
                document.type(),
                document.origin(),
                document.sector(),
                document.active()
        );
    }
}

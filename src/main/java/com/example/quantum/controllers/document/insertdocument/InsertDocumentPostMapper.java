package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.services.document.InsertDocumentPostInput;

public class InsertDocumentPostMapper {

    // Request → Input
    public static InsertDocumentPostInput toInput(InsertDocumentPostRequest request) {
        return new InsertDocumentPostInput(
                request.createdBy(),
                request.nameDocument(),
                request.content(),
                request.tempoDeRetencao(),
                request.type(),
                request.origin(),
                request.sector()
        );
    }

    // Domain → Response
    public static InsertDocumentPostResponse toResponse(Document document) {
        return new InsertDocumentPostResponse(
                document.documentId(),
                document.createdBy(),
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

package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.services.document.InsertDocumentPostInput;

public class InsertDocumentPostMapper {

    // Domain → Response
    public static InsertDocumentPostResponse toDocumentResponse(Document document) {
        return new InsertDocumentPostResponse(
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

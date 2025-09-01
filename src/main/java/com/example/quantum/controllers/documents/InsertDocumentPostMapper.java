package com.example.quantum.controllers.documents;

import com.example.quantum.domain.Document;

import java.util.UUID;

public class InsertDocumentPostMapper {

    public static Document toDocument(InsertDocumentPostRequest request){

        return new Document(
                UUID.randomUUID(),
                request.createdBy(),
                request.nameDocument(),
                request.content(),
                request.tempoDeRetencao(),
                true,
                request.type(),
                request.origin(),
                request.sector()
        );
    }
}

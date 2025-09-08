package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.domain.Document;

import java.util.UUID;

public class InsertDocumentPostMapper {

    //converte requisição para dominio
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

    
    //converte dominio para reponse
    public static InsertDocumentPostResponse toResponse(Document document) {
        return new InsertDocumentPostResponse(
                document.idDocument(),
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


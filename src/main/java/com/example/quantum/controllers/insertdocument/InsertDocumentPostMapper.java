package com.example.quantum.controllers.insertdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntity;

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

    //converte para entidade
    public static DocumentEntity toEntity(Document document) {
        DocumentEntity entity = new DocumentEntity();
        entity.setIdDocument(document.idDocument());
        entity.setCreateBy(document.createdBy());
        entity.setNameDocument(document.nameDocument());
        entity.setContent(document.content());
        entity.setTempoDeRetencao(document.tempoDeRetencao());
        entity.setActive(document.active());
        entity.setType(document.type());
        entity.setOrigin(document.origin());
        entity.setSector(document.sector());
        return entity;
    }



    
}


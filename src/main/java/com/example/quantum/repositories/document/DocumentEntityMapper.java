package com.example.quantum.repositories.document;

import com.example.quantum.domain.Document;

public class DocumentEntityMapper {
    public static Document toDocument(DocumentEntity entity) {
        return new Document(
                entity.getDocumentId(),
                entity.getCreatedBy(),
                entity.getNameDocument(),
                entity.getContent(),
                entity.getTempoDeRetencao(),
                entity.isActive(),
                entity.getType(),
                entity.getOrigin(),
                entity.getSector()
        );
    }

    public static DocumentEntity toEntity(Document document) {
        DocumentEntity entity = new DocumentEntity();
        entity.setDocumentId(document.documentId());
        entity.setCreatedBy(document.createdBy());
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

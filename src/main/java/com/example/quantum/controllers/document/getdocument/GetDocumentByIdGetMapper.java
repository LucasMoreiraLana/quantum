package com.example.quantum.controllers.document.getdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntity;


public class GetDocumentByIdGetMapper {

    public static GetDocumentByIdGetResponse toResponse(Document document){
        return new GetDocumentByIdGetResponse(
                document.documentId(),
                document.createdBy(),
                document.nameDocument(),
                document.content(),
                document.tempoDeRetencao(),
                document.active(),
                document.type(),
                document.origin(),
                document.sector()
        );
    }

    //entity -> domain
    public static Document toDomain(DocumentEntity entity){
        if(entity == null) return null;

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
}

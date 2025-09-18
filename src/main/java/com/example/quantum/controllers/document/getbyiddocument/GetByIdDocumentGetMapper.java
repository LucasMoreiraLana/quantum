package com.example.quantum.controllers.document.getbyiddocument;

import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntity;


public class GetByIdDocumentGetMapper {

    public static GetByIdDocumentGetResponse toResponse(Document document){
        return new GetByIdDocumentGetResponse(
                document.idDocument(),
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
                entity.getIdDocument(),
                entity.getCreateBy(),
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

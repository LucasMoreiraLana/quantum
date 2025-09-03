package com.example.quantum.mappers.document;

import com.example.quantum.repositories.document.DocumentEntity;
import com.example.quantum.domain.Document;;


public class GetDocumentMapper {



        public static Document toDomain(DocumentEntity entity) {
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

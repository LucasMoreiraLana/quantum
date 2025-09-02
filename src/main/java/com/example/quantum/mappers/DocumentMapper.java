package com.example.quantum.mappers;


import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntity;



public class DocumentMapper {



    public static Document toDocument(DocumentEntity documentEntity) {
        return new Document(
                documentEntity.getIdDocument(),
                documentEntity.getCreateBy(),
                documentEntity.getNameDocument(),
                documentEntity.getContent(),
                documentEntity.getTempoDeRetencao(),
                documentEntity.isActive(),
                documentEntity.getType(),
                documentEntity.getOrigin(),
                documentEntity.getSector()
        );
    }


}

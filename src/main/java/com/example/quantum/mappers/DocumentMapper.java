package com.example.quantum.mappers;

import com.example.quantum.controllers.documents.InsertDocumentPostRequest;
import com.example.quantum.controllers.documents.UpdateDocumentPutRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.domain.User;
import com.example.quantum.repositories.document.DocumentEntity;
import org.springframework.stereotype.Component;


public class DocumentMapper {



    public static Document toDocument(DocumentEntity documentEntity) {
        return new Document(
                documentEntity.getIdDocument(),
                documentEntity.getUser(),
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

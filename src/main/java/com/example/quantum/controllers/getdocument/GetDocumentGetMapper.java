package com.example.quantum.controllers.getdocument;

import com.example.quantum.repositories.document.DocumentEntity;

import java.util.stream.Collectors;

import com.example.quantum.domain.Document;
import com.example.quantum.controllers.getdocument.GetDocumentGetResponse;
import java.util.List;


public class GetDocumentGetMapper {



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

        public static List<GetDocumentGetResponse> toResponseList(List<Document> documents) {

        return documents.stream()
                .map(GetDocumentGetResponse::toResponse)
                .collect(Collectors.toList());
         }

         




}

package com.example.quantum.controllers.document.getdocument;

import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntity;
import com.example.quantum.services.document.GetDocumentServiceGetOutput; // NOVO IMPORT!

import java.util.Optional;


public class GetDocumentByIdGetMapper {


    public static GetDocumentByIdGetResponse toDocumentResponse(GetDocumentServiceGetOutput output){
        final Document document = output.document();

        // Trata o Optional<String> createdByName que vem do Service
        final String createdByName = output.createdByName().orElse("Usuário Desconhecido");

        return new GetDocumentByIdGetResponse(
                document.documentId(),
                document.createdBy(),
                document.nameDocument(),
                document.content(),
                document.tempoDeRetencao(),
                document.active(),
                document.type(),
                document.origin(),
                document.sector(),

                // NOVO CAMPO: Mapeia o nome buscado
                createdByName
        );
    }

    // 2. Método Entity -> Domain (Permanece o mesmo, mas a lógica de uso muda no Service)
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
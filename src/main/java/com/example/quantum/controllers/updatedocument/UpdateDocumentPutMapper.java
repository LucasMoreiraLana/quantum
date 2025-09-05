package com.example.quantum.controllers.updatedocument;

import com.example.quantum.repositories.document.DocumentEntity;

public class UpdateDocumentPutMapper {

    public static void updateEntityRequest(UpdateDocumentPutRequest request, DocumentEntity entity) {
        if (request.nameDocument() != null) {
            entity.setNameDocument(request.nameDocument());
        }
        if (request.content() != null) {
            entity.setContent(request.content());
        }
        if (request.tempoDeRetencao() != null && request.tempoDeRetencao() > 0) {
            entity.setTempoDeRetencao(request.tempoDeRetencao());
        }
        if (request.type() != null) {
            entity.setType(request.type());
        }
        if (request.origin() != null) {
            entity.setOrigin(request.origin());
        }
        if (request.sector() != null) {
            entity.setSector(request.sector());
        }
    }

    
}

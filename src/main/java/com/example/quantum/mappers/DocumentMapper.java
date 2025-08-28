package com.example.quantum.mappers;

import com.example.quantum.dtos.request.documents.DocumentCreateRequest;
import com.example.quantum.dtos.request.documents.DocumentUpdateRequest;
import com.example.quantum.dtos.response.documents.DocumentResponse;
import com.example.quantum.models.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public Document toEntity(DocumentCreateRequest request) {
        Document document = new Document();
        document.setUser(request.user());
        document.setNameDocument(request.nameDocument());
        document.setContent(request.content());
        document.setTempoDeRetencao(request.tempoDeRetencao());
        document.setType(request.type());
        document.setOrigin(request.origin());
        document.setSector(request.sector());
        document.setActive(true);
        return document;
    }

    public DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getIdDocument(),
                document.getUser(),
                document.getNameDocument(),
                document.getContent(),
                document.getTempoDeRetencao(),
                document.isActive(),
                document.getType(),
                document.getOrigin(),
                document.getSector()
        );
    }

    public void updateEntityFromDTO(DocumentUpdateRequest request, Document document) {
        if (request.nameDocument() != null) {
            document.setNameDocument(request.nameDocument());
        }
        if (request.content() != null) {
            document.setContent(request.content());
        }
        if (request.tempoDeRetencao() != null) {
            document.setTempoDeRetencao(request.tempoDeRetencao());
        }
        if (request.type() != null) {
            document.setType(request.type());
        }
        if (request.origin() != null) {
            document.setOrigin(request.origin());
        }
        if (request.sector() != null) {
            document.setSector(request.sector());
        }
    }
}

package com.example.quantum.services.documents;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.mappers.DocumentMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.controllers.documents.UpdateDocumentPutRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.repositories.document.DocumentEntity;

@Service
@Transactional
public class UpdateDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document update(UUID id, UpdateDocumentPutRequest updateDTO) {
        final var documentEntity = documentRepository.findById(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        
        updateEntityFromDTO(updateDTO, documentEntity);
        final var updatedDocument = documentRepository.save(documentEntity);
        return DocumentMapper.toDocument(updatedDocument);
    }


    private void updateEntityFromDTO(UpdateDocumentPutRequest request, DocumentEntity documentEntity) {
        if (request.nameDocument() != null) {
            documentEntity.setNameDocument(request.nameDocument());
        }
        if (request.content() != null) {
            documentEntity.setContent(request.content());
        }
        if (request.tempoDeRetencao() != null) {
            documentEntity.setTempoDeRetencao(request.tempoDeRetencao());
        }
        if (request.type() != null) {
            documentEntity.setType(request.type());
        }
        if (request.origin() != null) {
            documentEntity.setOrigin(request.origin());
        }
        if (request.sector() != null) {
            documentEntity.setSector(request.sector());
        }
    }
}

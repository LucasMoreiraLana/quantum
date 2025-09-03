package com.example.quantum.services.document;

import com.example.quantum.controllers.document.InsertDocumentPostMapper;
import com.example.quantum.controllers.document.InsertDocumentPostRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.mappers.document.InsertDocumentMapper;
import com.example.quantum.repositories.document.DocumentEntity;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CreateDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document create(InsertDocumentPostRequest createDTO) {
        final var document = InsertDocumentPostMapper.toDocument(createDTO);
        final var documentEntity = toEntity(document);

        
        if (documentEntity.getIdDocument() == null) {
            documentEntity.setIdDocument(UUID.randomUUID());
        }

        final var savedDocument = documentRepository.save(documentEntity);
        return InsertDocumentMapper.toDocument(savedDocument);
    }

    private DocumentEntity toEntity(Document document) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setIdDocument(document.idDocument()); 
        documentEntity.setCreateBy(document.createdBy()); 
        documentEntity.setNameDocument(document.nameDocument());
        documentEntity.setContent(document.content());
        documentEntity.setTempoDeRetencao(document.tempoDeRetencao());
        documentEntity.setType(document.type());
        documentEntity.setOrigin(document.origin());
        documentEntity.setSector(document.sector());
        documentEntity.setActive(true);
        return documentEntity;
    }
}

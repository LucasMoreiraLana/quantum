package com.example.quantum.services.documents;

import com.example.quantum.controllers.documents.InsertDocumentPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.controllers.documents.InsertDocumentPostRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntity;
import com.example.quantum.mappers.DocumentMapper;
import com.example.quantum.repositories.document.DocumentRepository;

@Service
@Transactional
public class CreateDocumentService {

    @Autowired
    private DocumentRepository documentRepository;


    public Document create(InsertDocumentPostRequest createDTO) {
        final var document = InsertDocumentPostMapper.toDocument(createDTO);
        final var documentEntity = toEntity(document);
        final var savedDocument = documentRepository.save(documentEntity);
        return DocumentMapper.toDocument(savedDocument);
    }

    private DocumentEntity toEntity(Document document) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setUserId(document.idDocument());
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

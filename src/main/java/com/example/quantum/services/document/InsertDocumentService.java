package com.example.quantum.services.document;

import com.example.quantum.controllers.insertdocument.InsertDocumentPostMapper;
import com.example.quantum.controllers.insertdocument.InsertDocumentPostRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class InsertDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document create(InsertDocumentPostRequest createDTO) {
        // Request → Domain
        final var document = InsertDocumentPostMapper.toDocument(createDTO);

        // Domain → Entity
        final var documentEntity = DocumentEntityMapper.toEntity(document);

        // Garantir ID se não existir
        if (documentEntity.getIdDocument() == null) {
            documentEntity.setIdDocument(UUID.randomUUID());
        }

        // Salvar no banco
        final var savedEntity = documentRepository.save(documentEntity);

        // Entity → Domain
        return DocumentEntityMapper.toDomain(savedEntity);
    }
}



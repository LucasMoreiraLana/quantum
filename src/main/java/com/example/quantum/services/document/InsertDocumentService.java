package com.example.quantum.services.document;

import com.example.quantum.controllers.document.insertdocument.InsertDocumentPostMapper;
import com.example.quantum.controllers.document.insertdocument.InsertDocumentPostRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InsertDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document create(InsertDocumentInput input) {
        // Input → Domain
        final var document = input.toDomain();

        // Domain → Entity
        final var entity = DocumentEntityMapper.toEntity(document);

        // Salvar no banco
        final var savedEntity = documentRepository.save(entity);

        // Entity → Domain
        return DocumentEntityMapper.toDomain(savedEntity);
    }
}



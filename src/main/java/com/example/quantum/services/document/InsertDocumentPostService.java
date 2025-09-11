package com.example.quantum.services.document;


import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InsertDocumentPostService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document create(InsertDocumentPostInput input) {
        // Input → Domain
        final var document = input.toDomain();

        // Domain → Entity
        final var entity = DocumentEntityMapper.toEntity(document);

        // Salvar no banco
        final var savedEntity = documentRepository.save(entity);

        // Entity → Domain
        return DocumentEntityMapper.toDocument(savedEntity);
    }
}



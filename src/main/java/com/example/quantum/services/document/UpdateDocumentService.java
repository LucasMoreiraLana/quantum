package com.example.quantum.services.document;

import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document update(UpdateDocumentPutInput input) {
        // Busca no banco
        final var existingEntity = documentRepository.findById(input.documentId())
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        // Atualiza os campos permitidos
        final var updatedDomain = new Document(
                input.documentId(),
                input.documentId(),
                input.nameDocument(),
                input.content(),
                input.tempoDeRetencao(),
                existingEntity.isActive(),
                input.type(),
                input.origin(),
                input.sector()
        );

        // Domain → Entity
        final var updatedEntity = DocumentEntityMapper.toEntity(updatedDomain);

        // Salva no banco
        final var savedEntity = documentRepository.save(updatedEntity);

        // Entity → Domain
        return DocumentEntityMapper.toDomain(savedEntity);
    }
}

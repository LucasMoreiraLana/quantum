package com.example.quantum.services.document;

import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateDocumentPutService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document updateDocument(UpdateDocumentPutInput input) {
        // Busca no banco
        final var existingEntity = documentRepository.findById(input.documentId())
                .orElseThrow(() -> new RuntimeException("Documento não encontrado!"));

        final var updatedDomain = new Document(
                input.documentId(),
                existingEntity.getCreatedBy(), // 🔒 nunca altera
                input.nameDocument(),
                input.content(),
                input.tempoDeRetencao(),
                input.active(),
                input.type(),
                input.origin(),
                input.sector()
        );


        // Domain → Entity
        final var updatedEntity = DocumentEntityMapper.toEntity(updatedDomain);

        // Salva no banco
        final var savedEntity = documentRepository.save(updatedEntity);

        // Entity → Domain
        return DocumentEntityMapper.toDocument(savedEntity);
    }
}

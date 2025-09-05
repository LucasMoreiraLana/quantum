package com.example.quantum.services.document;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.controllers.updatedocument.UpdateDocumentPutRequest;
import com.example.quantum.domain.Document;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.controllers.updatedocument.UpdateDocumentPutMapper;


@Service
@Transactional
public class UpdateDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document update(UUID id, UpdateDocumentPutRequest updateDTO) {
        final var documentEntity = documentRepository.findById(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));

        // Usa o mapper para atualizar
        UpdateDocumentPutMapper.updateEntityRequest(updateDTO, documentEntity);

        final var updatedDocument = documentRepository.save(documentEntity);

        // Converter Entity → Domain usando o mapper genérico
        return DocumentEntityMapper.toDomain(updatedDocument);
    }
}

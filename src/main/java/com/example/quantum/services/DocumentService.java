package com.example.quantum.services;

import com.example.quantum.models.Document;
import com.example.quantum.repositories.DocumentRepo;
import com.example.quantum.repositories.specs.DocumentSearchCriteria;
import com.example.quantum.repositories.specs.DocumentSpecs;
import com.example.quantum.dtos.request.documents.DocumentCreateRequest;
import com.example.quantum.dtos.request.documents.DocumentUpdateRequest;
import com.example.quantum.dtos.response.documents.DocumentResponse;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.mappers.DocumentMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Validated
public class DocumentService {
    
    @Autowired
    private final DocumentRepo documentRepo;
    @Autowired
    private final DocumentMapper documentMapper;

    public DocumentService(DocumentRepo documentRepo, DocumentMapper documentMapper) {
        this.documentRepo = documentRepo;
        this.documentMapper = documentMapper;
    }

    /**
     * Busca um documento por ID
     */
    public DocumentResponse findById(UUID id) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        return documentMapper.toResponse(document);
    }

    /**
     * Busca documentos com critérios dinâmicos e paginação
     */
    public Page<DocumentResponse> searchDocuments(DocumentSearchCriteria criteria, Pageable pageable) {
        return documentRepo.findAll(DocumentSpecs.withDynamicQuery(criteria), pageable)
            .map(documentMapper::toResponse);
    }

    /**
     * Lista todos os documentos ativos
     */
    public List<DocumentResponse> findAllActive() {
        DocumentSearchCriteria criteria = DocumentSearchCriteria.builder()
            .onlyActive(true)
            .build();
        return documentRepo.findAll(DocumentSpecs.withDynamicQuery(criteria))
            .stream()
            .map(documentMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Cria um novo documento
     */
    public DocumentResponse create(@Valid DocumentCreateRequest createDTO) {
        Document document = documentMapper.toEntity(createDTO);
        document = documentRepo.save(document);
        return documentMapper.toResponse(document);
    }

    /**
     * Atualiza um documento existente
     */
    public DocumentResponse update(UUID id, @Valid DocumentUpdateRequest updateDTO) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        
        documentMapper.updateEntityFromDTO(updateDTO, document);
        document = documentRepo.save(document);
        return documentMapper.toResponse(document);
    }

    /**
     * Exclusão lógica do documento
     */
    public void delete(UUID id) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        document.setActive(false);
        documentRepo.save(document);
    }
}

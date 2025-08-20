package com.example.quantum.services;

import com.example.quantum.dtos.DocumentCreateDTO;
import com.example.quantum.dtos.DocumentResponseDTO;
import com.example.quantum.dtos.DocumentUpdateDTO;
import com.example.quantum.models.Document;
import com.example.quantum.repositories.DocumentRepo;
import com.example.quantum.repositories.specs.DocumentSearchCriteria;
import com.example.quantum.repositories.specs.DocumentSpecs;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.mappers.DocumentMapper;
import jakarta.validation.Valid;
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
    
    private final DocumentRepo documentRepo;
    private final DocumentMapper documentMapper;

    public DocumentService(DocumentRepo documentRepo, DocumentMapper documentMapper) {
        this.documentRepo = documentRepo;
        this.documentMapper = documentMapper;
    }

    /**
     * Busca um documento por ID
     */
    public DocumentResponseDTO findById(UUID id) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        return documentMapper.toDTO(document);
    }

    /**
     * Busca documentos com critérios dinâmicos e paginação
     */
    public Page<DocumentResponseDTO> searchDocuments(DocumentSearchCriteria criteria, Pageable pageable) {
        return documentRepo.findAll(DocumentSpecs.withDynamicQuery(criteria), pageable)
            .map(documentMapper::toDTO);
    }

    /**
     * Lista todos os documentos ativos
     */
    public List<DocumentResponseDTO> findAllActive() {
        DocumentSearchCriteria criteria = DocumentSearchCriteria.builder()
            .onlyActive(true)
            .build();
        return documentRepo.findAll(DocumentSpecs.withDynamicQuery(criteria))
            .stream()
            .map(documentMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Cria um novo documento
     */
    public DocumentResponseDTO create(@Valid DocumentCreateDTO createDTO) {
        Document document = documentMapper.toEntity(createDTO);
        document = documentRepo.save(document);
        return documentMapper.toDTO(document);
    }

    /**
     * Atualiza um documento existente
     */
    public DocumentResponseDTO update(UUID id, @Valid DocumentUpdateDTO updateDTO) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        
        documentMapper.updateEntityFromDTO(updateDTO, document);
        document = documentRepo.save(document);
        return documentMapper.toDTO(document);
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

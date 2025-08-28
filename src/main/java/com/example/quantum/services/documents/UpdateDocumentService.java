package com.example.quantum.services.documents;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.quantum.mappers.DocumentMapper;
import com.example.quantum.repositories.DocumentRepo;
import com.example.quantum.dtos.request.documents.DocumentUpdateRequest;
import com.example.quantum.dtos.response.documents.DocumentResponse;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.models.Document;

import jakarta.validation.Valid;

@Service
@Transactional
@Validated
public class UpdateDocumentService {

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private DocumentMapper documentMapper;

    public DocumentResponse update(UUID id, @Valid DocumentUpdateRequest updateDTO) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        
        documentMapper.updateEntityFromDTO(updateDTO, document);
        document = documentRepo.save(document);
        return documentMapper.toResponse(document);
    }
}

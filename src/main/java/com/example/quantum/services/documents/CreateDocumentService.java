package com.example.quantum.services.documents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.dtos.request.documents.DocumentCreateRequest;
import com.example.quantum.dtos.response.documents.DocumentResponse;
import com.example.quantum.models.Document;
import com.example.quantum.mappers.DocumentMapper;
import com.example.quantum.repositories.DocumentRepo;

import jakarta.validation.Valid;

@Service
@Transactional
@Validated
public class CreateDocumentService {

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private DocumentMapper documentMapper;

    public DocumentResponse create(@Valid DocumentCreateRequest createDTO) {
        Document document = documentMapper.toEntity(createDTO);
        document = documentRepo.save(document);
        return documentMapper.toResponse(document);
    }
}

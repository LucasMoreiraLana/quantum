package com.example.quantum.services.documents;

import com.example.quantum.domain.Document;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.mappers.DocumentMapper;
import com.example.quantum.repositories.document.DocumentEntity;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Transactional
public class GetDocumentService {


    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentMapper documentMapper;

    public Document findById(UUID id) {
        DocumentEntity documentEntity = documentRepository.findByIdDocument(id)
                .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        return documentMapper.toDocument(documentEntity);
    }

}
package com.example.quantum.services.document;



import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class GetAllDocumentGetService {

    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(DocumentEntityMapper::toDocument) // Entity -> Domain
                .toList();
        }

    }



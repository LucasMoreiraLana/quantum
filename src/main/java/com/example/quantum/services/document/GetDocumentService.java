package com.example.quantum.services.document;



import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



    @Service
    public class GetDocumentService {

        @Autowired
        private DocumentRepository documentRepository;

        public List<Document> getAll() {
            return documentRepository.findAll()
                    .stream()
                    .map(DocumentEntityMapper::toDomain) // Entity -> Domain
                    .toList();
        }
    }



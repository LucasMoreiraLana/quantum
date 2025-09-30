package com.example.quantum.services.document;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.repositories.document.DocumentEntity;

@Service
@Transactional
public class DeleteDocumentDeleteService {
    
    @Autowired
    private DocumentRepository documentRepository;

    public void deleteDocument(UUID documentId) {
        DocumentEntity documentEntity = documentRepository.findById(documentId)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + documentId));
        documentEntity.setActive(false);
        documentRepository.save(documentEntity);
    }

}

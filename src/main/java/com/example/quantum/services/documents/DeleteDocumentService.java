package com.example.quantum.services.documents;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.repositories.document.DocumentEntity;

@Service
@Transactional
public class DeleteDocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;

    public void delete(UUID id) {
        DocumentEntity documentEntity = documentRepository.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        documentEntity.setActive(false);
        documentRepository.save(documentEntity);
    }

}

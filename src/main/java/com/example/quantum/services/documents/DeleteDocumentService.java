package com.example.quantum.services.documents;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.DocumentRepo;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.models.Document;

@Service
@Transactional
@Validated
public class DeleteDocumentService {
    
    @Autowired
    private DocumentRepo documentRepo;

    public void delete(UUID id) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        document.setActive(false);
        documentRepo.save(document);
    }

}

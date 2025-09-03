package com.example.quantum.services.document;


import com.example.quantum.domain.Document;
import com.example.quantum.mappers.document.InsertDocumentMapper;
import com.example.quantum.repositories.document.DocumentEntity;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class GetDocumentService {


    @Autowired
    private DocumentRepository documentRepository;



   public List<Document> findAllDocuments() {
        List<DocumentEntity> entities = documentRepository.findAll();
        return entities.stream()
                       .map(InsertDocumentMapper::toDocument)
                       .collect(Collectors.toList());
    }

}
package com.example.quantum.services.document;


import com.example.quantum.controllers.document.getdocumentbyid.GetDocumentByIdGetMapper;
import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdDocumentGetService {

    @Autowired
    private DocumentRepository documentRepository;

    public Optional<Document> execute(GetByIdDocumentGetInput input){
        return documentRepository.findById(input.documentId())
                .map(GetDocumentByIdGetMapper::toDomain);
    }
}

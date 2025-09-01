package com.example.quantum.controllers.documents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.services.documents.CreateDocumentService;
import com.example.quantum.domain.Document;

@RestController
@RequestMapping("/v1/documents")
public class InsertDocumentPostController {

    @Autowired
    private CreateDocumentService createDocumentService;

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody InsertDocumentPostRequest createRequest) {
        Document response = createDocumentService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}

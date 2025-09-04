package com.example.quantum.controllers.insertdocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quantum.domain.Document;
import com.example.quantum.services.document.InsertDocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class InsertDocumentPostController {

    @Autowired
    private InsertDocumentService createDocumentService;

    @PostMapping
    public ResponseEntity<InsertDocumentPostResponse> createDocument(
            @Valid @RequestBody InsertDocumentPostRequest createRequest) {
        
        // Service retorna Domain
        Document savedDocument = createDocumentService.create(createRequest);

        // Domain → Response
        InsertDocumentPostResponse response = InsertDocumentPostMapper.toResponse(savedDocument);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


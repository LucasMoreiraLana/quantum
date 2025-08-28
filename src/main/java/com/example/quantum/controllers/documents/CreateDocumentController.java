package com.example.quantum.controllers.documents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.services.documents.CreateDocumentService;
import com.example.quantum.dtos.request.documents.DocumentCreateRequest;
import com.example.quantum.dtos.response.documents.DocumentResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class CreateDocumentController {

    @Autowired
    private CreateDocumentService createDocumentService;

    @PostMapping("/create")
    public ResponseEntity<DocumentResponse> createDocument(@Valid @RequestBody DocumentCreateRequest createRequest) {
        DocumentResponse response = createDocumentService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}

package com.example.quantum.controllers.document.insertdocument;

import com.example.quantum.services.document.InsertDocumentPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class InsertDocumentPostController {

    @Autowired
    private InsertDocumentPostService insertDocumentPostService;

    @PostMapping
    public ResponseEntity<InsertDocumentPostResponse> create(@Valid @RequestBody InsertDocumentPostRequest request) {
        // Request → Input
        final var input = InsertDocumentPostMapper.toInput(request);

        // Service com Input → Domain
        final var document = insertDocumentPostService.createDocument(input);

        // Domain → Response
        final var response = InsertDocumentPostMapper.toResponse(document);

        return ResponseEntity.ok(response);
    }
}


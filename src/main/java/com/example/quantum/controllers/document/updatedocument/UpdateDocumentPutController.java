package com.example.quantum.controllers.document.updatedocument;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.quantum.services.document.UpdateDocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class UpdateDocumentPutController {

    @Autowired
    private UpdateDocumentService updateDocumentService;

    @PutMapping("/{id}")
    public ResponseEntity<UpdateDocumentPutResponse> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDocumentPutRequest request) {

        // Request → Input
        final var input = UpdateDocumentPutMapper.toInput(id, request);

        // Service com Input
        final var updated = updateDocumentService.update(input);

        // Domain → Response
        final var response = UpdateDocumentPutMapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }
}

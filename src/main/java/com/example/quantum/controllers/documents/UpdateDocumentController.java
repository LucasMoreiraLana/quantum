package com.example.quantum.controllers.documents;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.services.documents.UpdateDocumentService;
import com.example.quantum.dtos.request.documents.DocumentUpdateRequest;
import com.example.quantum.dtos.response.documents.DocumentResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/documents")
public class UpdateDocumentController {

    @Autowired
    private UpdateDocumentService updateDocumentService;

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody DocumentUpdateRequest updateRequest) {
        DocumentResponse response = updateDocumentService.update(id, updateRequest);
        return ResponseEntity.ok(response);
    }
}

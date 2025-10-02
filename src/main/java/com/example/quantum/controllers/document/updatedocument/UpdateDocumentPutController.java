package com.example.quantum.controllers.document.updatedocument;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.quantum.services.document.UpdateDocumentPutService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class UpdateDocumentPutController {

    @Autowired
    private UpdateDocumentPutService updateDocumentPutService;

    @PutMapping("/{documentId}")
    public ResponseEntity<UpdateDocumentPutResponse> updateDocument(
            @PathVariable UUID documentId,
            @Valid @RequestBody UpdateDocumentPutRequest request) {

        // Request → Input
        final var input = UpdateDocumentPutMapper.toInput(documentId, request);

        // Service com Input
        final var updateDocument = updateDocumentPutService.updateDocument(input);

        // Domain → Response
        final var response = UpdateDocumentPutMapper.toResponse(updateDocument);

        return ResponseEntity.ok(response);
    }
}

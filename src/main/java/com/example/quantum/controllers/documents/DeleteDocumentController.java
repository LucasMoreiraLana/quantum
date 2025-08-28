package com.example.quantum.controllers.documents;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.services.documents.DeleteDocumentService;

@RestController
@RequestMapping("/v1/documents")
public class DeleteDocumentController {

    @Autowired
    private DeleteDocumentService deleteDocumentService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        deleteDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

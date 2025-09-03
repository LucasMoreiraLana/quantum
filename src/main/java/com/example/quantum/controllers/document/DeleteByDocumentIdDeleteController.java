package com.example.quantum.controllers.document;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quantum.services.document.DeleteDocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class DeleteByDocumentIdDeleteController {

    @Autowired
    private DeleteDocumentService deleteDocumentService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@Valid @PathVariable UUID id) {
        deleteDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

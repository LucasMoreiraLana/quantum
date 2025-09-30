package com.example.quantum.controllers.document.deletedocument;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quantum.services.document.DeleteDocumentDeleteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class DeleteDocumentByIdDeleteController {

    @Autowired
    private DeleteDocumentDeleteService deleteDocumentDeleteService;

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@Valid @PathVariable UUID documentId) {
        deleteDocumentDeleteService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }

}

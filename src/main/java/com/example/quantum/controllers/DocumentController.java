package com.example.quantum.controllers;

import com.example.quantum.dtos.documents.DocumentCreateDTO;
import com.example.quantum.dtos.documents.DocumentResponseDTO;
import com.example.quantum.dtos.documents.DocumentUpdateDTO;
import com.example.quantum.services.DocumentService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/documents")
public class DocumentController {

    @Autowired
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/create")
    public ResponseEntity<DocumentResponseDTO> createDocument(@Valid @RequestBody DocumentCreateDTO createDTO) {
        DocumentResponseDTO response = documentService.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> getDocument(@PathVariable UUID id) {
        DocumentResponseDTO response = documentService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<DocumentResponseDTO>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nameDocument") String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<DocumentResponseDTO> response = documentService.searchDocuments(null, pageRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DocumentResponseDTO>> getActiveDocuments() {
        List<DocumentResponseDTO> response = documentService.findAllActive();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody DocumentUpdateDTO updateDTO) {
        DocumentResponseDTO response = documentService.update(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

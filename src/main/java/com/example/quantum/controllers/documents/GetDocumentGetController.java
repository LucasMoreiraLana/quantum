package com.example.quantum.controllers.documents;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.quantum.domain.Document;
import com.example.quantum.services.documents.GetDocumentService;


@RestController
@RequestMapping("/v1/documents")
public class GetDocumentGetController {
    @Autowired
    private GetDocumentService getDocumentService;

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable UUID id) {
        Document response = getDocumentService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<Document>> getAllDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nameDocument") String sortBy,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Document> response = getDocumentService.searchDocuments(null, pageRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Document>> getActiveDocuments() {
        List<Document> response = getDocumentService.findAllActive();
        return ResponseEntity.ok(response);
    }
}

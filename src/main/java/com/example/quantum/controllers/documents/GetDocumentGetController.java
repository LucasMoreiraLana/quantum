package com.example.quantum.controllers.documents;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

}

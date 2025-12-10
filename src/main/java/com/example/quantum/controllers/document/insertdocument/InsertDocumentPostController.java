package com.example.quantum.controllers.document.insertdocument;


import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.services.document.InsertDocumentPostInput;
import com.example.quantum.services.document.InsertDocumentPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID; // Para clareza

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class InsertDocumentPostController {

    private static final Logger log = LoggerFactory.getLogger(InsertDocumentPostController.class);

    @Autowired
    private InsertDocumentPostService insertDocumentPostService;

    @PostMapping
    public ResponseEntity<InsertDocumentPostResponse> createDocument(
            @Valid @RequestBody InsertDocumentPostRequest request,
            @AuthenticationPrincipal UserEntity loggedUser
    ) {

        final var createdBy = loggedUser.getUserId();
        final var documentId = UUID.randomUUID();

        final var input = new InsertDocumentPostInput(
                documentId,
                createdBy,
                request.nameDocument(),
                request.content(),
                request.tempoRetencao(),
                request.type().type(),
                request.type().origin(),
                request.sector()
        );

        // Service com Input → Domain
        final var document = insertDocumentPostService.createDocument(input);

        // Domain → Response
        final var response = InsertDocumentPostMapper.toDocumentResponse(document);

        return ResponseEntity.ok(response);
    }
}


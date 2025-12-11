package com.example.quantum.controllers.document.getdocument;


import com.example.quantum.services.document.GetDocumentByIdGetInput;
import com.example.quantum.services.document.GetDocumentByIdGetService;
import com.example.quantum.services.document.GetDocumentServiceGetOutput; // Assumindo que este é o DTO de Saída
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/documents")
public class GetDocumentByIdGetController {

    @Autowired
    private GetDocumentByIdGetService getDocumentByIdGetService;


    @GetMapping("/{documentId}")
    public ResponseEntity<GetDocumentByIdGetResponse> getDocumentById(@PathVariable UUID documentId) {
        final var input = new GetDocumentByIdGetInput(documentId);

        final Optional<GetDocumentServiceGetOutput> output = getDocumentByIdGetService.execute(input);

        return output
                // Mapeia o DTO de Service para o DTO de Resposta (incluindo createdByName)
                .map(GetDocumentByIdGetMapper::toDocumentResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
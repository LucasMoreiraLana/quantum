package com.example.quantum.controllers.document.insertdocument;


import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.services.document.InsertDocumentPostInput;
import com.example.quantum.services.document.InsertDocumentPostService;
import com.example.quantum.services.document.InsertDocumentServicePostOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/documents")
public class InsertDocumentPostController {

    private static final Logger log = LoggerFactory.getLogger(InsertDocumentPostController.class); // FALTAVA (Boa Prática)

    // O CAMPO AUTOWIRED ESTAVA FALTANDO! (ESSENCIAL)
    @Autowired
    private InsertDocumentPostService insertDocumentPostService;

    // O MAPPER DEVE ESTAR ACESSÍVEL/IMPORTADO CORRETAMENTE
    // Não precisa de @Autowired se o método for estático, mas a classe precisa estar acessível.

    @PostMapping
    public ResponseEntity<InsertDocumentPostResponse> createDocument(
            @Valid @RequestBody InsertDocumentPostRequest request,
            @AuthenticationPrincipal UserEntity loggedUser
    ) {
        log.info("Recebendo requisição para criar documento: {}", request.nameDocument());

        final var createdBy = loggedUser.getUserId();
        final var documentId = UUID.randomUUID();

        // 1. Construção do Input
        final var input = new InsertDocumentPostInput(
                documentId,
                createdBy,
                request.nameDocument(),
                request.content(),
                request.tempoDeRetencao(),
                request.type(),
                request.origin(),
                request.sector()
        );

        // 2. Chamada ao Service (que agora busca o nome do criador)
        final InsertDocumentServicePostOutput output = insertDocumentPostService.createDocument(input);

        // 3. Mapeamento para a Resposta
        final var response = InsertDocumentPostMapper.toDocumentResponse(
                output.document(),
                output.createdByName()
        );

        log.info("Documento {} criado com sucesso pelo usuário {}", documentId, output.createdByName());
        return ResponseEntity.ok(response);
    }
}
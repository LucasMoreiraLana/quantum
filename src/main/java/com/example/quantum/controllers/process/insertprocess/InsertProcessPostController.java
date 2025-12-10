package com.example.quantum.controllers.process.insertprocess;

import com.example.quantum.services.process.InsertProcessPostService;
import com.example.quantum.services.process.InsertProcessPostInput;
import com.example.quantum.repositories.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// ADICIONAR IMPORTS DO LOGGER
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID; // Para clareza

@RestController
@RequestMapping("/v1/processes")
public class InsertProcessPostController {

    // INICIALIZAÇÃO DO LOGGER
    private static final Logger log = LoggerFactory.getLogger(InsertProcessPostController.class);

    @Autowired
    private InsertProcessPostService insertProcessPostService;

    @PostMapping
    public ResponseEntity<InsertProcessPostResponse> createProcess(
            @Valid @RequestBody InsertProcessPostRequest request,
            @AuthenticationPrincipal UserEntity loggedUser
    ) {
        final var createdBy = loggedUser.getUserId();
        final var processId = UUID.randomUUID();  // ✅ Gera o UUID aqui

        final var input = new InsertProcessPostInput(
                processId,
                createdBy,
                request.nameProcess(),
                request.dateApproval(),
                request.dateConclusion(),
                request.sector(),
                request.cyclePDCA()
        );

        final var process = insertProcessPostService.insertProcess(input);
        final var response = InsertProcessPostMapper.toProcessResponse(process);

        return ResponseEntity.ok(response);
    }
}
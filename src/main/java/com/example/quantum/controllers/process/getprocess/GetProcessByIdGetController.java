package com.example.quantum.controllers.process.getprocess;


import com.example.quantum.services.process.GetByProcessIdGetInput;
import com.example.quantum.services.process.GetByProcessIdGetService;
import com.example.quantum.services.process.GetProcessServiceGetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.Optional; // Import para Optional

@RestController
@RequestMapping("/v1/processes")
public class GetProcessByIdGetController {

    @Autowired
    private GetByProcessIdGetService getByProcessIdGetService;

    @GetMapping("/{processId}")
    public ResponseEntity<?> getByProcessIdResponse(@PathVariable("processId") String processIdStr) {
        System.out.println(">>> DEBUG BACKEND: Recebido o ID: [" + processIdStr + "]");
        try {
            UUID processId = UUID.fromString(processIdStr.trim());
            GetByProcessIdGetInput input = new GetByProcessIdGetInput(processId);

            // Vamos ver se o Service encontra o dado
            Optional<GetProcessServiceGetOutput> output = getByProcessIdGetService.execute(input);

            if (output.isPresent()) {
                System.out.println(">>> DEBUG BACKEND: Processo encontrado no banco!");
                // TESTE: Comente a linha do Mapper e retorne um Map simples
                // Se isso funcionar com 200 OK, o erro está no GetProcessByIdGetMapper ou no DTO de resposta
                // Remova o return ResponseEntity.ok().body("OK...") e use:
                return output.map(GetProcessByIdGetMapper::toResponse)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.err.println(">>> DEBUG BACKEND Erro: " + e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

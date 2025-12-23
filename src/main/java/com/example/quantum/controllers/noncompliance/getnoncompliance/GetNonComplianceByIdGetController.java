package com.example.quantum.controllers.noncompliance.getnoncompliance;

import com.example.quantum.services.noncompliance.GetNonComplianceByIdGetInput;
import com.example.quantum.services.noncompliance.GetNonComplianceByIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/nc")
public class GetNonComplianceByIdGetController {

    @Autowired
    private GetNonComplianceByIdGetService getNonComplianceByIdGetService;

    @GetMapping("/{ncId}")
    public ResponseEntity<?> getNonComplianceByIdResponse(@PathVariable String ncId) {  // Mudado para String para validar manualmente

        try {
            UUID nonComplianceId = UUID.fromString(ncId);  // Tenta converter para UUID
            GetNonComplianceByIdGetInput input = new GetNonComplianceByIdGetInput(nonComplianceId);

            return getNonComplianceByIdGetService.execute(input)
                    .map(nonComplianceOutput -> ResponseEntity.ok(GetNonComplianceByIdGetMapper.toResponse(nonComplianceOutput)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("ID inválido: deve ser um UUID válido no formato 'xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx'.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno ao processar a requisição: " + e.getMessage());
        }
    }
}
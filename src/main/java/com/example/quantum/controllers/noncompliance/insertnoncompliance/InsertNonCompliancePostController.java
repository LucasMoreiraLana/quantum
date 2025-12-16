package com.example.quantum.controllers.noncompliance.insertnoncompliance;


import com.example.quantum.services.noncompliance.InsertNonCompliancePostService;
import com.example.quantum.services.noncompliance.InserNonComplianceServicePostOutput; // NOVO IMPORT!
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.quantum.controllers.noncompliance.insertnoncompliance.InsertNonCompliancePostMapper;

@RestController
@RequestMapping("/v1/nc")
public class InsertNonCompliancePostController {

    @Autowired
    private InsertNonCompliancePostService insertNonCompliancePostService;

    @PostMapping
    public ResponseEntity<InsertNonCompliancePostResponse> createNonCompliance(@Valid @RequestBody InsertNonCompliancePostRequest request){

        final var input = InsertNonCompliancePostMapper.toInput(request);

        // CORRIGIDO: O tipo agora corresponde ao que o Service retorna
        final InserNonComplianceServicePostOutput nonComplianceOutput = insertNonCompliancePostService.createNonCompliance(input);

        // Mapeia o DTO de Saída (Output) para o DTO de Resposta HTTP (Response)
        final var response = InsertNonCompliancePostMapper.toResponse(nonComplianceOutput);

        return ResponseEntity.ok(response);
    }
}
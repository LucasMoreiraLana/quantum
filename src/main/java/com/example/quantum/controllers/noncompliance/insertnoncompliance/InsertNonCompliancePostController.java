package com.example.quantum.controllers.noncompliance.insertnoncompliance;


import com.example.quantum.services.noncompliance.InsertNonCompliancePostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.controllers.noncompliance.insertnoncompliance.InsertNonCompliancePostMapper;

@RestController
@RequestMapping("/v1/nc")
public class InsertNonCompliancePostController {

    @Autowired
    private InsertNonCompliancePostService insertNonCompliancePostService;

    @PostMapping
    public ResponseEntity<InsertNonCompliancePostResponse> createNonCompliance(@Valid @RequestBody InsertNonCompliancePostRequest request){

        final var input = InsertNonCompliancePostMapper.toInput(request);

        final var nonCompliance = insertNonCompliancePostService.createNonCompliance(input);

        final var response = InsertNonCompliancePostMapper.toResponse(nonCompliance);

        return ResponseEntity.ok(response);
    }
}

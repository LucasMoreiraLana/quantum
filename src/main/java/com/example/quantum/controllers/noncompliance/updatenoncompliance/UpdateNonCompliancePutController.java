package com.example.quantum.controllers.noncompliance.updatenoncompliance;


import com.example.quantum.services.noncompliance.UpdateNonCompliancePutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/nc")
public class UpdateNonCompliancePutController {

    @Autowired
    private UpdateNonCompliancePutService updateNonCompliancePutService;

    @PutMapping("/{ncId}")
    public ResponseEntity<UpdateNonCompliancePutResponse> updateNonCompliance(
            @PathVariable UUID ncId,
            @Valid @RequestBody UpdateNonCompliancePutRequest request){

        final var input = UpdateNonCompliancePutMapper.toNonComplianceInput(ncId, request);

        final var updatedNonCompliance = updateNonCompliancePutService.updateNonCompliance(input);

        final var response = UpdateNonCompliancePutMapper.toNonComplianceResponse(updatedNonCompliance);

        return ResponseEntity.ok(response);

    }
}

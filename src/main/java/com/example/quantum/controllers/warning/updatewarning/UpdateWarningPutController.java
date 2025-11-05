package com.example.quantum.controllers.warning.updatewarning;

import com.example.quantum.services.warning.UpdateWarningPutService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/warnings")
public class UpdateWarningPutController {

    @Autowired
    private UpdateWarningPutService updateWarningPutService;

    @PutMapping("/{warningId}")
    public ResponseEntity<UpdateWarningPutResponse> updateWarning(
            @PathVariable UUID warningId,
            @Valid @RequestBody UpdateWarningPutRequest request){

        final var input = UpdateWarningPutMapper.toWarningInput(warningId, request);

        final var updateWarning = updateWarningPutService.updateWarning(input);

        final var response = UpdateWarningPutMapper.toWarningResponse(updateWarning);

        return ResponseEntity.ok(response);
    }

}

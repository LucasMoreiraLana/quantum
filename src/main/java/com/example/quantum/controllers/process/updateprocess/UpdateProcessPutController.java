package com.example.quantum.controllers.process.updateprocess;


import com.example.quantum.services.process.UpdateProcessPutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/processes")
public class UpdateProcessPutController {

    @Autowired
    private UpdateProcessPutService updateProcessPutService;

    @PutMapping("/{processId}")
    public ResponseEntity<UpdateProcessPutResponse> updateProcess(
            @PathVariable UUID processId,
            @Valid @RequestBody UpdateProcessPutRequest request) {

        final var input = UpdateProcessPutMapper.toInput(processId, request);

        final var updatedProcess = updateProcessPutService.updateProcess(input);

        final var response = UpdateProcessPutMapper.toResponse(updatedProcess);

        return ResponseEntity.ok(response);

    }
}

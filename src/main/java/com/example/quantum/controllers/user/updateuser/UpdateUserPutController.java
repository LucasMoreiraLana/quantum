package com.example.quantum.controllers.user.updateuser;

import java.util.UUID;

import com.example.quantum.services.user.UpdateUserPutInput;
import com.example.quantum.services.user.UpdateUserPutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UpdateUserPutController {

    @Autowired
    private UpdateUserPutService updateUserPutService;

    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserPutResponse> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserPutRequest request) {

        // Request → Input
        final var input = UpdateUserPutMapper.toInput(userId, request);

        // Service → Domain
        final var updated = updateUserPutService.updateUser(input);

        // Domain → Response
        final var response = UpdateUserPutMapper.toResponse(updated);

        return ResponseEntity.ok(response); // 👍 corrigido
    }

}


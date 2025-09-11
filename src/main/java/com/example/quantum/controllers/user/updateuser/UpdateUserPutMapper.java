package com.example.quantum.controllers.user.updateuser;

import java.util.UUID;

import com.example.quantum.domain.Document;
import com.example.quantum.domain.User;
import com.example.quantum.services.user.UpdateUserPutInput;

public class UpdateUserPutMapper {

    // Request → Input
    public static UpdateUserPutInput toInput(UUID id, UpdateUserPutRequest request) {
        return new UpdateUserPutInput(
                id,
                request.username(),
                request.password(),
                request.email(),
                request.sector(),
                request.position()
        );
    }

    // Domain → Response
    public static UpdateUserPutResponse toResponse(User user) {
        return new UpdateUserPutResponse(
                user.username(),
                user.password(),
                user.email(),
                user.active(),
                user.sector(),
                user.position()
        );
    }
}

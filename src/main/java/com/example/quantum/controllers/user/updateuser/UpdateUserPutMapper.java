package com.example.quantum.controllers.user.updateuser;


import com.example.quantum.repositories.user.UserEntity;

public class UpdateUserPutMapper {

    public static void userEntityRequest(UpdateUserPutRequest request, UserEntity entity) {
        if (request.username() != null) {
            entity.setUsername(request.username());
        }
        if (request.password() != null) {
            entity.setPassword(request.password());
        }
        if (request.email() != null) {
            entity.setEmail(request.email());
        }

        entity.setActive(request.active());

        if (request.sector() != null) {
            entity.setSector(request.sector());
        }
        if (request.position() != null) {
            entity.setPosition(request.position()); // Enum User.Position
        }
    }
}

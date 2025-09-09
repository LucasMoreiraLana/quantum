package com.example.quantum.controllers.user.insertuser;

import com.example.quantum.domain.User;

import java.util.UUID;

public class InsertUserPostMapper {

    //converte requisição para domain
    public static User toUser(InsertUserPostRequest request){
        return new User (
                UUID.randomUUID(),
                request.username(),
                request.password(),
                request.email(),
                true,
                request.sector(),
                request.position()
        );
    }

    public static InsertUserPostResponse toResponse(User user){
        return new InsertUserPostResponse(
                user.idUser(),
                user.username(),
                user.password(),
                user.email(),
                user.sector(),
                user.position()
        );
    }
}

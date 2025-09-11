package com.example.quantum.controllers.user.insertuser;

import com.example.quantum.domain.User;
import com.example.quantum.services.user.InsertUserPostInput;

import java.util.UUID;

public class InsertUserPostMapper {

    //converte requisição para domain
    public static InsertUserPostInput toInput(InsertUserPostRequest request){
        return new InsertUserPostInput (
                UUID.randomUUID(),
                request.username(),
                request.password(),
                request.email(),
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

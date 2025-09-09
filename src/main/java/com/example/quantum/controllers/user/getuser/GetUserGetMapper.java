package com.example.quantum.controllers.user.getuser;

import com.example.quantum.domain.User;

public class GetUserGetMapper {

    public static GetUserGetResponse toResponse(User user){
        return new GetUserGetResponse(
                user.idUser(),
                user.username(),
                user.email(),
                user.active(),
                user.sector(),
                user.position()
        );
    }
}

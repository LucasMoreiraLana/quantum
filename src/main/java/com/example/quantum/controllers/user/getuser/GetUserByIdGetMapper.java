package com.example.quantum.controllers.user.getuser;

import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntity;

public class GetUserByIdGetMapper {

    public static GetUserByIdGetResponse toUserByIdResponse(User user){
        return new GetUserByIdGetResponse(
            user.userId(),
            user.username(),
            user.email(),
            user.active(),
            user.sector(),
            user.position()
        );
    }

    //entity -> domain
    public static User toDomain(UserEntity entity){
        if(entity == null) return null;

        return new User(
                entity.getUserId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.isActive(),
                entity.getSector(),
                entity.getPosition()
        );
    }

}

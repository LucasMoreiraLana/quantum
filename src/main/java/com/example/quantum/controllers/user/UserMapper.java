package com.example.quantum.controllers.user;

import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.domain.User;


public class UserMapper {



    public static User toUser(UserEntity userEntity) {
        return new User(
                userEntity.getIdUser(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.isActive(),
                userEntity.getSector(),
                userEntity.getPosition());
    }
}

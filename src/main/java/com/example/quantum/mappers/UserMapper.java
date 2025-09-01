package com.example.quantum.mappers;

import com.example.quantum.repositories.user.UserEntity;
import org.springframework.stereotype.Component;

import com.example.quantum.controllers.users.InsertUserPostRequest;
import com.example.quantum.controllers.users.UpdateUserPutRequest;
import com.example.quantum.domain.User;


public class UserMapper {



    public static User toUser(UserEntity userEntity) {
        return new User(
                userEntity.getIdUser(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.isActive(),
                userEntity.getSector(),
                userEntity.getPosition()
        );
    }
}

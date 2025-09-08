package com.example.quantum.repositories.user;

import com.example.quantum.domain.User;


public class UserMapperEntity {



    public static User toUser(UserEntity entity) {
        return new User(
                entity.getIdUser(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.isActive(),
                entity.getSector(),
                entity.getPosition()
        );
    }

    public static UserEntity toEntity(User user){
        UserEntity entity = new UserEntity();
        entity.setIdUser(user.idUser());
        entity.setUsername(user.username());
        entity.setPassword(user.password());
        entity.setEmail(user.email());
        entity.setActive(user.active());
        entity.setSector(user.sector());
        entity.setPosition(user.position());
        return entity;

    }
}

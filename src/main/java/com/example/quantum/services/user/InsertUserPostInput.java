package com.example.quantum.services.user;

import com.example.quantum.domain.User;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record InsertUserPostInput(

        UUID idUser,
        String username,
        String password,
        String email,
        Sector sector,
        User.Position position

){

    public User toDomain(){
        return new User(
                UUID.randomUUID(),
                this.username,
                this.password,
                this.email,
                true,
                this.sector,
                this.position
        );
    }
}

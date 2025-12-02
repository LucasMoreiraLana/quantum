package com.example.quantum.services.user;

import com.example.quantum.domain.User;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record UpdateUserPutInput(

        UUID userId,
        String username,
        String password,
        String email,
        boolean active,
        Sector sector,
        User.Position position

){}

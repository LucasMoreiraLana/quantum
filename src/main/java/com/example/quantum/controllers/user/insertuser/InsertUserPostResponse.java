package com.example.quantum.controllers.user.insertuser;

import com.example.quantum.domain.User;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record InsertUserPostResponse(
        UUID userId,
        String username,
        String password,
        String email,
        Sector sector,
        User.Position position

) {}

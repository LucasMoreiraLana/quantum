package com.example.quantum.controllers.user.getuser;


import com.example.quantum.domain.User;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record GetAllUserGetResponse(
        UUID userId,
        String username,
        String email,
        boolean active,
        Sector sector,
        User.Position position
){}
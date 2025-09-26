package com.example.quantum.controllers.user.getbyiduser;

import com.example.quantum.domain.User;
import com.example.quantum.enums.Sector;

import java.util.UUID;

public record GetByIdUserGetResponse(
    UUID userId,
    String userName,
    String email,
    boolean active,
    Sector sector,
    User.Position position

){}

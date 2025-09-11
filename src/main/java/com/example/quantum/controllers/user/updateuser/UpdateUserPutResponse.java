package com.example.quantum.controllers.user.updateuser;

import com.example.quantum.domain.User;
import com.example.quantum.enums.Sector;

public record UpdateUserPutResponse(
        String userName,
        String password,
        String email,
        boolean isActive,
        Sector sector,
        User.Position position
) {}

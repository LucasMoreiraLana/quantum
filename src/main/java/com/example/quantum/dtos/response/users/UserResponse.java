package com.example.quantum.dtos.response.users;

import java.util.UUID;

import com.example.quantum.enums.Position;
import com.example.quantum.enums.Sector;

public record UserResponse(
    UUID idUser,
    String username,
    String email,
    boolean active,
    Sector sector,
    Position position
) {}

package com.example.quantum.dtos.users;

import java.util.UUID;

import com.example.quantum.enums.Position;
import com.example.quantum.enums.Sector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID idUser;
    private String username;
    private String email;
    private boolean active;
    private Sector sector;
    private Position position;
}

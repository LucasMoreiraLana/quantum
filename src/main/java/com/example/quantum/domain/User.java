package com.example.quantum.domain;

import java.util.UUID;


import com.example.quantum.enums.Sector;


public record User(
    UUID userId,
    String username,
    String password,
    String email,
    boolean active,
    Sector sector,
    Position position
) {

    public enum Position {

        ADMINISTRADOR,
        DIRETOR,
        GESTOR,
        ENGENHEIRO,
        ANALISTA,
        ESTAGIARIO

    }

}

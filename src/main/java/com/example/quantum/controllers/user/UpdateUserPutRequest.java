package com.example.quantum.controllers.user;


import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.quantum.domain.User.Position;

public record UpdateUserPutRequest(

    @NotBlank(message = "O nome de usuário é obrigatório")
    String username,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotNull(message = "O setor do usuário precisa ser informado!")
    Sector sector,

    @NotNull(message = "A posição do usuário precisa ser informada!")
    Position position,

    boolean active
) {}

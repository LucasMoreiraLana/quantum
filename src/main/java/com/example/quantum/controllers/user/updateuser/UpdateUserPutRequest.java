package com.example.quantum.controllers.user.updateuser;


import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.quantum.domain.User.Position;
import jakarta.validation.constraints.Size;

public record UpdateUserPutRequest(

    @NotBlank(message = "O nome de usuário é obrigatório")
    String username,

    @Size(min=6, message = "A senha deve ter no mínimo 6 caracteres!")
    String password,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    boolean active,

    @NotNull(message = "O setor do usuário precisa ser informado!")
    Sector sector,

    @NotNull(message = "A posição do usuário precisa ser informada!")
    Position position

) {}

package com.example.quantum.dtos.users;

import com.example.quantum.enums.Position;
import com.example.quantum.enums.Sector;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    
    @NotBlank(message = "O nome de usuário é obrigatório")
    private String username;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "O setor do usuário precisa ser informado!")
    private Sector sector;

    @NotNull(message = "A posição do usuário precisa ser informada!")
    private Position position;

    private boolean active;
}

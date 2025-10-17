package com.example.quantum.repositories.user;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.quantum.enums.Sector;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.quantum.domain.User.Position;



@Document(collection = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
    
    @Id
    private UUID userId = UUID.randomUUID();

    @NotBlank(message = "O nome de usuário é obrigatório")
    private String username;
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    private boolean active = true;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "O setor do usuário precisa ser informado!")
    private Sector sector;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "A posição do usuário precisa ser informada!")
    private Position position;

}

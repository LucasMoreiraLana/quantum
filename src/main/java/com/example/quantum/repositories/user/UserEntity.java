package com.example.quantum.repositories.user;

import java.util.UUID;
import com.example.quantum.enums.Position;
import com.example.quantum.enums.Sector;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "idUser")
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUser;

    @NotBlank(message = "O nome de usuário é obrigatório")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    private boolean active = true;

    @NotNull(message = "O setor do usuário precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Sector sector;

    @NotNull(message = "A posição do usuário precisa ser informada!")
    @Enumerated(EnumType.STRING)
    private Position position;

    public UserEntity(String username, String password, String email, Sector sector, Position position) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.sector = sector;
        this.position = position;
    }


}

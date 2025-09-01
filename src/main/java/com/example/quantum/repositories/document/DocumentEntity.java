package com.example.quantum.repositories.document;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;
import com.example.quantum.repositories.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class DocumentEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UUID userId;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idDocument;

    @NotBlank(message = "O nome do documento não pode ser vazio!")
    private String nameDocument;

    @NotBlank(message = "A descrição do documento não pode ser vazia!")
    @Size(max = 5000)
    private String content;

    @Positive(message = "O tempo de retenção deve ser maior que zero!")
    private int tempoDeRetencao;

    private boolean active = true;

    @NotNull(message = "O tipo do documento precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Document.Type type;

    @NotNull(message = "A origem do documento precisa ser informada!")
    @Enumerated(EnumType.STRING)
    private Document.Origin origin;

    @NotNull(message = "O setor do documento precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Sector sector;

}

package com.example.quantum.models;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Document {

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
    private DocumentType type;

    @NotNull(message = "A origem do documento precisa ser informada!")
    @Enumerated(EnumType.STRING)
    private Origin origin;

    @NotNull(message = "O setor do documento precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Sector sector;

}

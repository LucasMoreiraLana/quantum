package com.example.quantum.repositories.document;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


import java.util.UUID;

@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
@Getter
@Setter
@NoArgsConstructor
public class DocumentEntity {

    @Id
    private UUID idDocument;

    private UUID createBy;

    @NotBlank(message = "O nome do documento não pode ser vazio!")
    private String nameDocument;

    @NotBlank(message = "A descrição do documento não pode ser vazia!")
    @Size(max = 5000)
    private String content;

    @Positive(message = "O tempo de retenção deve ser maior que zero!")
    private int tempoDeRetencao;

    private boolean active = true;

    @NotNull(message = "O tipo do documento precisa ser informado!")
    private Document.Type type; // Enums são suportados sem @Enumerated

    @NotNull(message = "A origem do documento precisa ser informada!")
    private Document.Origin origin;

    @NotNull(message = "O setor do documento precisa ser informado!")
    private Sector sector;

}

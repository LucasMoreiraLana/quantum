package com.example.quantum.dtos.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCreateDTO {
    
    @NotBlank(message = "O nome do documento não pode ser vazio!")
    private String nameDocument;

    @NotBlank(message = "A descrição do documento não pode ser vazia!")
    @Size(max = 5000)
    private String content;

    @Positive(message = "O tempo de retenção deve ser maior que zero!")
    private int tempoDeRetencao;

    @NotNull(message = "O tipo do documento precisa ser informado!")
    private DocumentType type;

    @NotNull(message = "A origem do documento precisa ser informada!")
    private Origin origin;

    @NotNull(message = "O setor do documento precisa ser informado!")
    private Sector sector;
}

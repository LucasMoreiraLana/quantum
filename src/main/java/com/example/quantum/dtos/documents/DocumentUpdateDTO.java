package com.example.quantum.dtos.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUpdateDTO {
    private String nameDocument;
    private String content;
    private Integer tempoDeRetencao;
    private DocumentType type;
    private Origin origin;
    private Sector sector;
}

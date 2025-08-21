package com.example.quantum.dtos.documents;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDTO {
    private UUID idDocument;
    private String nameDocument;
    private String content;
    private int tempoDeRetencao;
    private boolean active;
    private DocumentType type;
    private Origin origin;
    private Sector sector;
}

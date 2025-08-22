package com.example.quantum.mappers;

import com.example.quantum.dtos.documents.DocumentCreateDTO;
import com.example.quantum.dtos.documents.DocumentResponseDTO;
import com.example.quantum.dtos.documents.DocumentUpdateDTO;
import com.example.quantum.models.Document;
import org.springframework.stereotype.Component;


@Component
public class DocumentMapper {

    public Document toEntity(DocumentCreateDTO dto) {
        Document document = new Document();
        document.setNameDocument(dto.getNameDocument());
        document.setContent(dto.getContent());
        document.setTempoDeRetencao(dto.getTempoDeRetencao());
        document.setType(dto.getType());
        document.setOrigin(dto.getOrigin());
        document.setSector(dto.getSector());
        document.setActive(true);
        return document;
    }

    public DocumentResponseDTO toDTO(Document document) {
        return DocumentResponseDTO.builder()
            .idDocument(document.getIdDocument())
            .nameDocument(document.getNameDocument())
            .content(document.getContent())
            .tempoDeRetencao(document.getTempoDeRetencao())
            .active(document.isActive())
            .type(document.getType())
            .origin(document.getOrigin())
            .sector(document.getSector())
            .build();
    }

    public void updateEntityFromDTO(DocumentUpdateDTO dto, Document document) {
        

        if (dto.getNameDocument() != null) {
            document.setNameDocument(dto.getNameDocument());
        }
        if (dto.getContent() != null) {
            document.setContent(dto.getContent());
        }
        if (dto.getTempoDeRetencao() != null) {
            document.setTempoDeRetencao(dto.getTempoDeRetencao());
        }
        if (dto.getType() != null) {
            document.setType(dto.getType());
        }
        if (dto.getOrigin() != null) {
            document.setOrigin(dto.getOrigin());
        }
        if (dto.getSector() != null) {
            document.setSector(dto.getSector());
        }
    }
}

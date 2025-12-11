package com.example.quantum.services.document;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Sector;

import java.util.UUID;


public record InsertDocumentPostInput(

        UUID documentId,
        UUID createdBy,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        DocumentType type,
        DocumentOrigin origin,
        Sector sector

) {

    public Document toDomain() {
        return new Document(
                this.documentId,  // id gerado aqui
                this.createdBy,
                this.nameDocument,
                this.content,
                this.tempoDeRetencao,
                true,
                this.type,
                this.origin,
                this.sector
        );
    }

}

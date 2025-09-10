package com.example.quantum.services.document;

import com.example.quantum.domain.Document;
import com.example.quantum.enums.Sector;

import java.util.UUID;


public record InsertDocumentPostInput(

        UUID createdBy,
        String nameDocument,
        String content,
        int tempoDeRetencao,
        Document.Type type,
        Document.Origin origin,
        Sector sector

) {

    public Document toDomain() {
        return new Document(
                UUID.randomUUID(),  // id gerado aqui
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

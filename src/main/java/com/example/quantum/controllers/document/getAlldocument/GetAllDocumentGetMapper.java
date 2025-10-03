package com.example.quantum.controllers.document.getAlldocument;


import java.util.stream.Collectors;
import java.util.List;
import com.example.quantum.domain.Document;


public class GetAllDocumentGetMapper {

        public static GetAllDocumentGetResponse toResponse(Document document) {
        return new GetAllDocumentGetResponse(
                document.documentId(),
                document.createdBy(),
                document.nameDocument(),
                document.content(),
                document.tempoDeRetencao(),
                document.active(),
                document.type(),
                document.origin(),
                document.sector()
            );
        }

        public static List<GetAllDocumentGetResponse> toResponseList(List<Document> documents) {
                return documents.stream()
                        .map(GetAllDocumentGetMapper::toResponse)
                        .collect(Collectors.toList());
        }

}

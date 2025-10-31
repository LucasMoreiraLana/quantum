package com.example.quantum.controllers.document.getdocument;


import java.util.stream.Collectors;
import java.util.List;
import com.example.quantum.domain.Document;


public class GetAllDocumentGetMapper {

        public static GetAllDocumentGetResponse toDocumentResponse(Document document) {
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

        public static List<GetAllDocumentGetResponse> toResponseDocumentList(List<Document> documents) {
                return documents.stream()
                        .map(GetAllDocumentGetMapper::toDocumentResponse)
                        .collect(Collectors.toList());
        }

}

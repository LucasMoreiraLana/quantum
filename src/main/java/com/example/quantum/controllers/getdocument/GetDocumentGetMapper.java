package com.example.quantum.controllers.getdocument;


import java.util.stream.Collectors;
import java.util.List;
import com.example.quantum.domain.Document;


public class GetDocumentGetMapper {


        public static GetDocumentGetResponse toResponse(Document document) {
        return new GetDocumentGetResponse(
                document.nameDocument(),
                document.content(),
                document.tempoDeRetencao(),
                document.active(),
                document.type(),
                document.origin(),
                document.sector()
        );
        }

        public static List<GetDocumentGetResponse> toResponseList(List<Document> documents) {
        return documents.stream()
                .map(GetDocumentGetMapper::toResponse)
                .collect(Collectors.toList());
        }

       
        



}

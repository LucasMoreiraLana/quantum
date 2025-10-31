package com.example.quantum.controllers.document.getdocument;


import com.example.quantum.services.document.GetDocumentByIdGetInput;
import com.example.quantum.services.document.GetDocumentByIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/documents")
public class GetDocumentByIdGetController {

    @Autowired
    private GetDocumentByIdGetService getDocumentByIdGetService;

    @GetMapping("/{documentId}")
    public ResponseEntity<GetDocumentByIdGetResponse> getDocumentByIdResponse (@PathVariable UUID documentId){

        GetDocumentByIdGetInput input = new GetDocumentByIdGetInput(documentId);

        return getDocumentByIdGetService.execute(input)
                .map(document -> ResponseEntity.ok(GetDocumentByIdGetMapper.toDocumentResponse(document)))
                .orElse(ResponseEntity.notFound().build());
    }
}

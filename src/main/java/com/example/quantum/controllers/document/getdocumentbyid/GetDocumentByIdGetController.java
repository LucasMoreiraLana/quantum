package com.example.quantum.controllers.document.getdocumentbyid;


import com.example.quantum.services.document.GetByIdDocumentGetInput;
import com.example.quantum.services.document.GetByIdDocumentGetService;
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
    private GetByIdDocumentGetService getByIdDocumentGetService;

    @GetMapping("/{documentId}")
    public ResponseEntity<GetDocumentByIdGetResponse> getByIdResponse (@PathVariable UUID documentId){

        GetByIdDocumentGetInput input = new GetByIdDocumentGetInput(documentId);

        return getByIdDocumentGetService.execute(input)
                .map(document -> ResponseEntity.ok(GetDocumentByIdGetMapper.toResponse(document)))
                .orElse(ResponseEntity.notFound().build());
    }
}

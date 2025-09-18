package com.example.quantum.controllers.document.getbyiddocument;


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
public class GetByIdDocumentGetController {

    @Autowired
    private GetByIdDocumentGetService getByIdDocumentGetService;

    @GetMapping("/{idDocument}")
    public ResponseEntity<GetByIdDocumentGetResponse> getByIdResponse (@PathVariable UUID idDocument){

        GetByIdDocumentGetInput input = new GetByIdDocumentGetInput(idDocument);

        return getByIdDocumentGetService.execute(input)
                .map(document -> ResponseEntity.ok(GetByIdDocumentGetMapper.toResponse(document)))
                .orElse(ResponseEntity.notFound().build());
    }
}

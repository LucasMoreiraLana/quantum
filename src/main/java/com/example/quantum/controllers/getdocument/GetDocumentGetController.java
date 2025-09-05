package com.example.quantum.controllers.getdocument;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.quantum.domain.Document;
import com.example.quantum.services.document.GetDocumentService;
import java.util.List;



@RestController
@RequestMapping("/v1/documents")
public class GetDocumentGetController {
    
    @Autowired
    private GetDocumentService getDocumentService;

    @GetMapping
public ResponseEntity<List<GetDocumentGetResponse>> getAllDocuments() {
    List<Document> documents = getDocumentService.getAll();
    return ResponseEntity.ok(GetDocumentGetMapper.toResponseList(documents));
}

}

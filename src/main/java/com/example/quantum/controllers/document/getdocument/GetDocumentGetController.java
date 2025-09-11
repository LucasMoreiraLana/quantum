package com.example.quantum.controllers.document.getdocument;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.quantum.domain.Document;
import com.example.quantum.services.document.GetDocumentGetService;
import java.util.List;



@RestController
@RequestMapping("/v1/documents")
public class GetDocumentGetController {
    
    @Autowired
    private GetDocumentGetService getDocumentGetService;

    @GetMapping
public ResponseEntity<List<GetDocumentGetResponse>> getAllDocuments() {
    List<Document> documents = getDocumentGetService.getAllDocuments();
    return ResponseEntity.ok(GetDocumentGetMapper.toResponseList(documents));
}

}

package com.example.quantum.controllers.user.insertuser;

import com.example.quantum.controllers.document.insertdocument.InsertDocumentPostMapper;
import com.example.quantum.domain.User;
import com.example.quantum.services.user.InsertUserPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/v1/users")
public class InsertUserPostController {

    @Autowired
    private InsertUserPostService insertUserPostService;

    @PostMapping
    public ResponseEntity<InsertUserPostResponse> create(@Valid @RequestBody InsertUserPostRequest request) {

        //request -> input
        final var input = InsertUserPostMapper.toInput(request);

        final var user = insertUserPostService.createUser(input);

        final var response = InsertUserPostMapper.toResponse(user);

        return ResponseEntity.ok(response);
    }
}



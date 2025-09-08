package com.example.quantum.controllers.user.insertuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.domain.User;
import com.example.quantum.services.user.CreateUserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/v1/users")
public class InsertUserPostController {
    

    @Autowired
    private CreateUserService createUserService;

    @PostMapping("/create")
    public ResponseEntity<InsertUserPostResponse> createUser(@Valid @RequestBody InsertUserPostRequest createRequest) {

        //service -> domain
        User savedUser = createUserService.create(createRequest);
        //domain -> response
        InsertUserPostResponse response = InsertUserPostMapper.toResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

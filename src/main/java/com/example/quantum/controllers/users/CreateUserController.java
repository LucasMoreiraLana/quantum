package com.example.quantum.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.dtos.request.users.UserCreateRequest;
import com.example.quantum.dtos.response.users.UserResponse;
import com.example.quantum.services.users.CreateUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class CreateUserController {
    

    @Autowired
    private CreateUserService createUserService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest createRequest) {
        UserResponse response = createUserService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

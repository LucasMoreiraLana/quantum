package com.example.quantum.controllers.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.quantum.domain.User;
import com.example.quantum.services.users.CreateUserService;


@RestController
@RequestMapping("/v1/users")
public class InsertUserPostController {
    

    @Autowired
    private CreateUserService createUserService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody InsertUserPostRequest createRequest) {
        User response = createUserService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

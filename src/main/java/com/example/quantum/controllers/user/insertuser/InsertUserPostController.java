package com.example.quantum.controllers.user.insertuser;

import com.example.quantum.domain.User;
import com.example.quantum.services.user.InsertUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/users")
public class InsertUserPostController {

    @Autowired
    private InsertUserService createUserService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody InsertUserPostRequest request) {
        User createdUser = createUserService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}



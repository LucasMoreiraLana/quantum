package com.example.quantum.controllers.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.quantum.domain.User;
import com.example.quantum.services.user.GetUserService;


@RestController
@RequestMapping("/v1/users")
public class GetUserGetController {
    
    @Autowired
    private GetUserService getUserService;


    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> response = getUserService.findAllUsers();
        return ResponseEntity.ok(response);
    }


}
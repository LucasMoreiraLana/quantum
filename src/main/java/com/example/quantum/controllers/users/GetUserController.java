package com.example.quantum.controllers.users;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.quantum.services.users.GetUserService;
import com.example.quantum.domain.User;


@RestController
@RequestMapping("/v1/users")
public class GetUserController {
    
    @Autowired
    private GetUserService getUserService;


    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        User response = getUserService.findById(id);
        return ResponseEntity.ok(response);
    }


}
package com.example.quantum.controllers.user.getuser;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.quantum.domain.User;
import com.example.quantum.services.user.GetAllUserGetService;


@RestController
@RequestMapping("/v1/users")
public class GetUserGetController {
    
    @Autowired
    private GetAllUserGetService getAllUserGetService;


    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> response = getAllUserGetService.toResponse();
        return ResponseEntity.ok(response);
    }


}
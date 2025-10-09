package com.example.quantum.controllers.user.getuser;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.quantum.domain.User;
import com.example.quantum.services.user.GetUserGetService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/v1/users")
public class GetUserGetController {
    
    @Autowired
    private GetUserGetService getUserGetService;


    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> response = getUserGetService.toResponse();
        return ResponseEntity.ok(response);
    }


}
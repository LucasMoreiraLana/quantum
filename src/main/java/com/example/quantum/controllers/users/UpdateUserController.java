package com.example.quantum.controllers.users;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.quantum.dtos.request.users.UserUpdateRequest;
import com.example.quantum.dtos.response.users.UserResponse;
import com.example.quantum.services.users.UpdateUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users")
public class UpdateUserController {
    
    @Autowired
    private UpdateUserService updateUserService;


    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserResponse response = updateUserService.update(id, updateRequest);
        return ResponseEntity.ok(response);
    }

}

package com.example.quantum.controllers.user.deleteuser;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quantum.services.user.DeleteUserByIdDeleteService;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/v1/users")
public class DeleteUserByIdDeleteController {
    
    @Autowired
    private DeleteUserByIdDeleteService deleteUserByIdDeleteService;

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        deleteUserByIdDeleteService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}

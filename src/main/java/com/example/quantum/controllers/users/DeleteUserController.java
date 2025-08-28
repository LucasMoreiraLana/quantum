package com.example.quantum.controllers.users;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.quantum.services.users.DeleteUserService;


@RestController
@RequestMapping("/v1/users")
public class DeleteUserController {
    
    @Autowired
    private DeleteUserService deleteUserService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        deleteUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

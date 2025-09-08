package com.example.quantum.controllers.user.deleteuser;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quantum.services.user.DeleteUserService;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/v1/users")
public class DeleteByUserIdDeleteController {
    
    @Autowired
    private DeleteUserService deleteUserService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        deleteUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

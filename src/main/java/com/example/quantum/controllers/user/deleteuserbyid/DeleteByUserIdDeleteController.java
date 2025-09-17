package com.example.quantum.controllers.user.deleteuserbyid;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quantum.services.user.DeleteByIdUserDeleteService;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/v1/users")
public class DeleteByUserIdDeleteController {
    
    @Autowired
    private DeleteByIdUserDeleteService deleteByIdUserDeleteService;

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID idUser) {
        deleteByIdUserDeleteService.deleteUser(idUser);
        return ResponseEntity.noContent().build();
    }

}

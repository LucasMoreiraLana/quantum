package com.example.quantum.controllers.process.deleteprocessbyid;

import com.example.quantum.services.process.DeleteByIdProcessDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/v1/process")
public class DeleteProcessByIdDeleteController {

    @Autowired
    private DeleteByIdProcessDeleteService deleteByIdProcessDeleteService;

    @DeleteMapping("/{idProcess}")
    public ResponseEntity<Void> deleteProcess(@PathVariable UUID idProcess){
        deleteByIdProcessDeleteService.deleteProcess(idProcess);
        return ResponseEntity.noContent().build();
    }
}

package com.example.quantum.controllers.process.deleteprocessbyid;

import com.example.quantum.services.process.DeleteByProcessIdDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/v1/processes")
public class DeleteProcessDeleteByIdController {

    @Autowired
    private DeleteByProcessIdDeleteService deleteByProcessIdDeleteService;

    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> deleteProcess(@PathVariable UUID processId){
        deleteByProcessIdDeleteService.deleteProcess(processId);
        return ResponseEntity.noContent().build();
    }
}

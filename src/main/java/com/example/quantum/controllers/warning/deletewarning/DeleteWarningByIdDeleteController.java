package com.example.quantum.controllers.warning.deletewarning;


import com.example.quantum.services.warning.DeleteWarningByIdDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/warnings")
public class DeleteWarningByIdDeleteController {

    @Autowired
    private DeleteWarningByIdDeleteService deleteWarningByIdDeleteService;

    @DeleteMapping("/{warningId}")
    public ResponseEntity<Void> deleteWarning(@PathVariable UUID warningId){
        deleteWarningByIdDeleteService.deleteWarning(warningId);
        return ResponseEntity.noContent().build();
    }
}

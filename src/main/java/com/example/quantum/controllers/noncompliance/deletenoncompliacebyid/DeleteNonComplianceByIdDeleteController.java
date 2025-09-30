package com.example.quantum.controllers.noncompliance.deletenoncompliacebyid;


import com.example.quantum.services.noncompliance.DeleteNonComplianceByIdDeleteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/nc")
public class DeleteNonComplianceByIdDeleteController {

    @Autowired
    private DeleteNonComplianceByIdDeleteService deleteNonComplianceByIdDeleteService;

    @DeleteMapping("/{ncId}")
    public ResponseEntity<Void> deleteNonCompliance(@Valid @PathVariable UUID ncId){
        deleteNonComplianceByIdDeleteService.deleteNonCompliance(ncId);
        return ResponseEntity.noContent().build();
    }

}

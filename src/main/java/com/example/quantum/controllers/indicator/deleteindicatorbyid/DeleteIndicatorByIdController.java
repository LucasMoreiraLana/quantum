package com.example.quantum.controllers.indicator.deleteindicatorbyid;


import com.example.quantum.services.indicator.DeleteIndicatorByIdDeleteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/indicators")
public class DeleteIndicatorByIdController {

    @Autowired
    private DeleteIndicatorByIdDeleteService deleteIndicatorByIdDeleteService;

    @DeleteMapping("/{indicatorId}")
    public ResponseEntity<Void> deleteIndicator(@Valid @PathVariable UUID indicatorId){
        deleteIndicatorByIdDeleteService.deleteIndicator(indicatorId);
        return ResponseEntity.noContent().build();
    }
}

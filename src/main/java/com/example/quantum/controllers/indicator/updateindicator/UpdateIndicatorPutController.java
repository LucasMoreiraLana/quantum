package com.example.quantum.controllers.indicator.updateindicator;

import com.example.quantum.services.indicator.UpdateIndicatorPutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/indicators")
public class UpdateIndicatorPutController {

    @Autowired
    private UpdateIndicatorPutService updateIndicatorPutService;

    @PutMapping("/{indicatorId}")
    public ResponseEntity<UpdateIndicatorPutResponse> updateIndicator(
            @PathVariable UUID indicatorId,
            @Valid @RequestBody UpdateIndicatorPutRequest request) {

        final var input = UpdateIndicatorPutMapper.toIndicatorInput(indicatorId, request);

        final var updateIndicator = updateIndicatorPutService.updateIndicator(input);

        final var response = UpdateIndicatorPutMapper.toIndicatorResponse(updateIndicator);

        return ResponseEntity.ok(response);
    }

}

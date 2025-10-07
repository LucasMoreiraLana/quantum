package com.example.quantum.controllers.indicator.insertindicator;


import com.example.quantum.services.indicator.InsertIndicatorPostService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/indicators")
public class InsertIndicatorPostController {

    @Autowired
    private InsertIndicatorPostService insertIndicatorPostService;

    @PostMapping
    public ResponseEntity<InsertIndicatorPostResponse> createIndicator(@Valid @RequestBody InsertIndicatorPostRequest request){

        final var input = InsertIndicatorPostMapper.toInput(request);

        final var indicator = insertIndicatorPostService.createIndicator(input);

        final var response = InsertIndicatorPostMapper.toResponse(indicator);

        return ResponseEntity.ok(response);
    }
}

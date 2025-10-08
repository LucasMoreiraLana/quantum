package com.example.quantum.controllers.indicator.getindicatorbyid;


import com.example.quantum.services.indicator.GetIndicatorByIdGetInput;
import com.example.quantum.services.indicator.GetIndicatorByIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/indicators")
public class GetIndicatorByIdGetController {

    @Autowired
    private GetIndicatorByIdGetService getIndicatorByIdService;

    @GetMapping("/{indicatorId}")
    public ResponseEntity<GetIndicatorByIdGetResponse> getIndicatorByIdResponse (@PathVariable UUID indicatorId){

        GetIndicatorByIdGetInput input = new GetIndicatorByIdGetInput(indicatorId);

        return getIndicatorByIdService.execute(input)
                .map(indicator -> ResponseEntity.ok(GetIndicatorByIdGetMapper.toResponse(indicator)))
                .orElse(ResponseEntity.notFound().build());
    }
}

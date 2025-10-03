package com.example.quantum.controllers.indicator.getallindicator;



import com.example.quantum.domain.Indicator;
import com.example.quantum.services.indocator.GetAllIndicatorGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/indicators")
public class GetAllIndicatorGetController {

    @Autowired
    private GetAllIndicatorGetService getAllIndicatorGetService;

    @GetMapping
    public ResponseEntity<List<GetAllIndicatorGetResponse>> getAllIndicators(){
        List<Indicator> indicators = getAllIndicatorGetService.getAllIndicator();
        return ResponseEntity.ok(GetAllIndicatorGetMapper.toResponseList(indicators));
    }


}

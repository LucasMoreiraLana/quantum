package com.example.quantum.controllers.noncompliance.getnoncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.services.noncompliance.GetAllNonComplianceGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/v1/nc")
public class GetAllNonComplianceGetController {

    @Autowired
    private GetAllNonComplianceGetService getAllNonComplianceGetService;

    @GetMapping
    public ResponseEntity<List<GetAllNonComplianceGetResponse>> getAllNonCompliance(){
        List<NonCompliance> nonComplaices = getAllNonComplianceGetService.getAllNonCompliance();
        return ResponseEntity.ok(GetAllNonComplianceGetMapper.toResponseList(nonComplaices));
    }
}

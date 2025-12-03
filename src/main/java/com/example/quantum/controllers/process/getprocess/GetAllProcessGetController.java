package com.example.quantum.controllers.process.getprocess;


import com.example.quantum.domain.Process;
import com.example.quantum.services.process.GetAllProcessGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/processes")
public class GetAllProcessGetController {

    @Autowired
    private GetAllProcessGetService getAllProcessGetService;

    @GetMapping
    public ResponseEntity<List<GetAllProcessGetResponse>> getAllProcess(){
        List<Process> process = getAllProcessGetService.getAllProcess();
        return ResponseEntity.ok(GetAllProcessGetMapper.toResponseList(process));
    }
}

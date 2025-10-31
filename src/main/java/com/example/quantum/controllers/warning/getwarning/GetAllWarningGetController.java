package com.example.quantum.controllers.warning.getwarning;


import com.example.quantum.domain.Warning;
import com.example.quantum.services.warning.GetAllWarningGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class GetAllWarningGetController {

    @Autowired
    private GetAllWarningGetService getAllWarningGetService;

    @GetMapping
    public ResponseEntity<List<GetAllWarningGetResponse>> findAllWarning() {
        List<Warning> warnings = getAllWarningGetService.getAllWarning();
        return ResponseEntity.ok(GetAllWarningGetMapper.toResponseWarningsList(warnings));
    }


}

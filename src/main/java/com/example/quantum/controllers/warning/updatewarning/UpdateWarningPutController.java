package com.example.quantum.controllers.warning.updatewarning;

import com.example.quantum.services.warning.UpdateWarningPutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/warnings")
public class UpdateWarningPutController {

    @Autowired
    private UpdateWarningPutService updateWarningPutService;



}

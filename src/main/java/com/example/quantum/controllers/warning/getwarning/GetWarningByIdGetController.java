package com.example.quantum.controllers.warning.getwarning;


import com.example.quantum.domain.Warning;
import com.example.quantum.services.warning.GetWarningByIdGetInput;
import com.example.quantum.services.warning.GetWarningByIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/warnings")
public class GetWarningByIdGetController {


    @Autowired
    private GetWarningByIdGetService getWarningByIdGetService;

    @GetMapping("/{warningId}")
    public ResponseEntity<GetWarningByIdGetResponse> getWarningByIdResponse (@PathVariable UUID warningId){

        GetWarningByIdGetInput input = new GetWarningByIdGetInput(warningId);

        return getWarningByIdGetService.execute(input)
                .map(warning -> ResponseEntity.ok(GetWarningByIdGetMapper.toWarningByIdResponse(warning)))
                .orElse(ResponseEntity.notFound().build());
    }

}

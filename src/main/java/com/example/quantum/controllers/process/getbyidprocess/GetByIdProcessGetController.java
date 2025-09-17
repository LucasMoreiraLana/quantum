package com.example.quantum.controllers.process.getbyidprocess;



import com.example.quantum.services.process.GetByIdProcessGetInput;
import com.example.quantum.services.process.GetByIdProcessGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/process")
public class GetByIdProcessGetController {

    @Autowired
    private GetByIdProcessGetService getByIdProcessGetService;

    @GetMapping("/{id}")
    public ResponseEntity<GetByIdProcessGetResponse> getByIdResponse(@PathVariable UUID id){

        GetByIdProcessGetInput input = new GetByIdProcessGetInput(id);

        return getByIdProcessGetService.execute(input)
                .map(process -> ResponseEntity.ok(GetByIdProcessGetMapper.toResponse(process)))
                .orElse(ResponseEntity.notFound().build());

    }
}

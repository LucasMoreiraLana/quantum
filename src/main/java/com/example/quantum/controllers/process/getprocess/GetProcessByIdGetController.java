package com.example.quantum.controllers.process.getprocess;


import com.example.quantum.services.process.GetByProcessIdGetInput;
import com.example.quantum.services.process.GetByProcessIdGetService;
import com.example.quantum.services.process.GetProcessServiceGetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.Optional; // Import para Optional

@RestController
@RequestMapping("/v1/processes")
public class GetProcessByIdGetController {

    @Autowired
    private GetByProcessIdGetService getByProcessIdGetService;

    @GetMapping("/{processId}")
    public ResponseEntity<GetProcessByIdGetResponse> getByProcessIdResponse(@PathVariable UUID processId){

        GetByProcessIdGetInput input = new GetByProcessIdGetInput(processId);

        // O Service retorna o Optional<GetProcessoServiceOutput>
        Optional<GetProcessServiceGetOutput> output = getByProcessIdGetService.execute(input);

        // O Mapper mapeia o Output (com o nome) para o Response final
        return output.map(GetProcessByIdGetMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
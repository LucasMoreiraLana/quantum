package com.example.quantum.controllers.process.getbyidprocess;



import com.example.quantum.services.process.GetByProcessIdGetInput;
import com.example.quantum.services.process.GetByProcessIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/process")
public class GetByProcessIdGetController {

    @Autowired
    private GetByProcessIdGetService getByProcessIdGetService;

    @GetMapping("/{ProcessId}")
    public ResponseEntity<GetByProcessIdGetResponse> getByProcessIdResponse(@PathVariable UUID processId){

        GetByProcessIdGetInput input = new GetByProcessIdGetInput(processId);

        return getByProcessIdGetService.execute(input)
                .map(process -> ResponseEntity.ok(GetByProcessIdGetMapper.toResponse(process)))
                .orElse(ResponseEntity.notFound().build());

    }
}

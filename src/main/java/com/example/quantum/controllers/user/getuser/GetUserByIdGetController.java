package com.example.quantum.controllers.user.getuser;


import com.example.quantum.services.user.GetUserByIdGetInput;
import com.example.quantum.services.user.GetUserByIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class GetUserByIdGetController {

    @Autowired
    private GetUserByIdGetService getUserByIdGetService;

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserByIdGetResponse> getByIdResponse (@PathVariable UUID userId){

        GetUserByIdGetInput input = new GetUserByIdGetInput(userId);

        return getUserByIdGetService.execute(input)
                .map(user -> ResponseEntity.ok(GetUserByIdGetMapper.toUserByIdResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}

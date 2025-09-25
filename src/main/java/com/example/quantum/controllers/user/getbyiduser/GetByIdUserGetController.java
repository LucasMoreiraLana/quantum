package com.example.quantum.controllers.user.getbyiduser;


import com.example.quantum.services.user.GetByUserIdGetInput;
import com.example.quantum.services.user.GetByUserIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class GetByIdUserGetController {

    @Autowired
    private GetByUserIdGetService getByUserIdGetService;

    @GetMapping("/{idUser}")
    public ResponseEntity<GetByIdUserGetResponse> getByIdResponse (@PathVariable UUID idUser){

        GetByUserIdGetInput input = new GetByUserIdGetInput(idUser);

        return getByUserIdGetService.execute(input)
                .map(user -> ResponseEntity.ok(GetByIdUserGetMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}

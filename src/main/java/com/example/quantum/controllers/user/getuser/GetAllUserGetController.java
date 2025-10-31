package com.example.quantum.controllers.user.getuser;

import java.util.List;

import com.example.quantum.controllers.indicator.getindicator.GetAllIndicatorGetMapper;
import com.example.quantum.controllers.indicator.getindicator.GetAllIndicatorGetResponse;
import com.example.quantum.services.user.GetAllUserGetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.quantum.domain.User;


@RestController
@RequestMapping("/v1/users")
public class GetAllUserGetController {
    
    @Autowired
    private GetAllUserGetService getAllUserGetService;


    @GetMapping
    public ResponseEntity<List<GetAllUserGetResponse>> findAllUsers() {
        List<User> users = getAllUserGetService.getAllUser();
        return ResponseEntity.ok(GetAllUserGetMapper.toResponseUsersList(users));
    }


}
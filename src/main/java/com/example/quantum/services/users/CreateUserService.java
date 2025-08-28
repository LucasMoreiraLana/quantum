package com.example.quantum.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.repositories.UserRepo;
import com.example.quantum.mappers.UserMapper;
import com.example.quantum.dtos.request.users.UserCreateRequest;
import com.example.quantum.dtos.response.users.UserResponse;
import com.example.quantum.models.User;
import jakarta.validation.Valid;


@Service
@Transactional
@Validated
public class CreateUserService {

    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private UserMapper userMapper;

    public UserResponse create(@Valid UserCreateRequest createRequest) {
        User user = userMapper.toEntity(createRequest);
        user = userRepo.save(user);
        return userMapper.toDTO(user);
    }
}

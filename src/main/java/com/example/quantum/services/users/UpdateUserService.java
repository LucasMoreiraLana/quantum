package com.example.quantum.services.users;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.quantum.dtos.request.users.UserUpdateRequest;
import com.example.quantum.dtos.response.users.UserResponse;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.models.User;
import com.example.quantum.repositories.UserRepo;
import com.example.quantum.mappers.UserMapper;

import jakarta.validation.Valid;


@Service
@Transactional
@Validated
public class UpdateUserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;

    public UserResponse update(UUID id, @Valid UserUpdateRequest updateRequest) {
        User user = userRepo.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        
        user = userMapper.updateEntity(user, updateRequest);
        user = userRepo.save(user);
        return userMapper.toDTO(user);
    }
}

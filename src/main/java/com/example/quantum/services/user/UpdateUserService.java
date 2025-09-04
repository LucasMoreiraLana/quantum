package com.example.quantum.services.user;

import java.util.UUID;

import com.example.quantum.repositories.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quantum.controllers.user.UpdateUserPutRequest;
import com.example.quantum.controllers.user.UserMapper;
import com.example.quantum.domain.User;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.repositories.user.UserRepository;


@Service
@Transactional
public class UpdateUserService {

    @Autowired
    private UserRepository userRepository;

    public User update(UUID id, UpdateUserPutRequest updateRequest) {
        final var userEntity = userRepository.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        
        final var updatedUser = updateEntity(userEntity, updateRequest);
        final var user = userRepository.save(updatedUser);
        return UserMapper.toUser(user);
    }

    private UserEntity updateEntity(UserEntity userEntity, UpdateUserPutRequest request) {
        userEntity.setUsername(request.username());
        userEntity.setEmail(request.email());
        userEntity.setSector(request.sector());
        userEntity.setPosition(request.position());
        userEntity.setActive(request.active());
        return userEntity;
    }
}

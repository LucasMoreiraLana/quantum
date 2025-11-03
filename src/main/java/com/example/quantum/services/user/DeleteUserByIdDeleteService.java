package com.example.quantum.services.user;

import java.util.UUID;

import com.example.quantum.repositories.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.exceptions.UserNotFoundException;


@Service
public class DeleteUserByIdDeleteService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUser(UUID userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + userId));
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }
}

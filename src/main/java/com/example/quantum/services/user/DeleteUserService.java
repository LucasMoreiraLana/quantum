package com.example.quantum.services.user;

import java.util.UUID;

import com.example.quantum.repositories.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.exceptions.UserNotFoundException;


@Service
@Transactional
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    public void delete(UUID id) {
        UserEntity userEntity = userRepository.findByIdUser(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }
}

package com.example.quantum.services.user;

import java.util.UUID;

import com.example.quantum.repositories.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.exceptions.UserNotFoundException;


@Service
public class DeleteByIdUserDeleteService {

    @Autowired
    private UserRepository userRepository;

    public void deleteUser(UUID idUser) {
        UserEntity userEntity = userRepository.findByIdUser(idUser)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + idUser));
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }
}

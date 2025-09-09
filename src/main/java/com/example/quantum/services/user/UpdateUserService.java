package com.example.quantum.services.user;

import java.util.UUID;

import com.example.quantum.controllers.user.updateuser.UpdateUserPutMapper;
import com.example.quantum.repositories.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quantum.controllers.user.updateuser.UpdateUserPutRequest;
import com.example.quantum.repositories.user.UserMapperEntity;
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

        UpdateUserPutMapper.userEntityRequest(updateRequest, userEntity);

        final var updatedUser = userRepository.save(userEntity);
        return UserMapperEntity.toUser(updatedUser);
    }

}

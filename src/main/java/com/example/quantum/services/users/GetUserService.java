package com.example.quantum.services.users;

import com.example.quantum.domain.User;
import com.example.quantum.exceptions.UserNotFoundException;
import com.example.quantum.mappers.UserMapper;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional
public class GetUserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(UUID id) {
        UserEntity userEntity = userRepository.findByIdUser(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        return UserMapper.toUser(userEntity);
    }


}

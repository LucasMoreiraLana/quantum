package com.example.quantum.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.controllers.user.InsertUserPostRequest;
import com.example.quantum.controllers.user.UserMapper;
import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntity;



@Service
@Transactional
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    public User create(InsertUserPostRequest createRequest) {
        final var userEntity = toEntity(createRequest);
        final var user = userRepository.save(userEntity);
        return UserMapper.toUser(user);
    }

    private UserEntity toEntity(InsertUserPostRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.username());
        userEntity.setPassword(request.password());
        userEntity.setEmail(request.email());
        userEntity.setSector(request.sector());
        userEntity.setPosition(request.position());
        userEntity.setActive(true);
        return userEntity;
    }
}

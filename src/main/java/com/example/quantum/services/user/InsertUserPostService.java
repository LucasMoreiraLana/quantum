package com.example.quantum.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.controllers.user.insertuser.InsertUserPostMapper;
import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.domain.User;

@Service
@Transactional
public class InsertUserPostService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User createRequest) {
        // transforma a request em domain
        final var userDomain = InsertUserPostMapper.toInput(createRequest);

        // transforma domain em entity
        final var userEntity = UserEntityMapper.toEntity(userDomain);

        // persiste no banco
        final var savedEntity = userRepository.save(userEntity);

        // retorna domain novamente
        return UserEntityMapper.toUser(savedEntity);
    }
}

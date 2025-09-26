package com.example.quantum.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.domain.User;

@Service
@Transactional
public class InsertUserPostService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(InsertUserPostInput input) {
        // transforma a request em domain
        final var user = input.toDomain();

        // transforma domain em entity
        final var entity = UserEntityMapper.toEntity(user);

        if (userRepository.existsByUsername(user.username())) {
            throw new IllegalArgumentException("Já existe um usuário com esse nome!");
        }

        // persiste no banco
        final var savedEntity = userRepository.save(entity);

        // retorna domain novamente
        return UserEntityMapper.toUser(savedEntity);
    }
}

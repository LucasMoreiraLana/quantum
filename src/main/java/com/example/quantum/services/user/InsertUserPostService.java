package com.example.quantum.services.user;

import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InsertUserPostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(InsertUserPostInput input) {

        // transforma request -> domain
        final var user = input.toDomain();

        // valida duplicidade
        if (userRepository.existsByUsername(user.username())) {
            throw new IllegalArgumentException("Já existe um usuário com esse nome!");
        }

        if (userRepository.existsByEmail(user.email())){
            throw new IllegalArgumentException(("Já existe um usuário com esse email!"));
        }

        // transforma domain -> entity
        final var entity = UserEntityMapper.toEntity(user);

        // ✅ Criptografa a senha antes de salvar
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        // persiste no banco
        final var savedEntity = userRepository.save(entity);

        // retorna domain novamente
        return UserEntityMapper.toUser(savedEntity);
    }
}

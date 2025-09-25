package com.example.quantum.services.user;


import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserPutService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(UpdateUserPutInput input) {
        // Busca no banco
        final var existingEntity = userRepository.findById(input.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se já existe outro usuário com o mesmo nome
        if (userRepository.existsByUsernameAndUserIdNot(input.username(), input.userId())) {
            throw new RuntimeException("Já existe um usuário com esse nome");
        }

        // Atualiza os campos permitidos
        final var updatedUser = new User(
                existingEntity.getUserId(),
                input.username(),
                input.password(),
                input.email(),
                true,
                input.sector(),
                input.position()
        );

        // Domain → Entity
        final var updatedEntity = UserEntityMapper.toEntity(updatedUser);

        // Salva no banco
        final var savedEntity = userRepository.save(updatedEntity);

        // Entity → Domain
        return UserEntityMapper.toUser(savedEntity);
    }
}

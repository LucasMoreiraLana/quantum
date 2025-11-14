package com.example.quantum.services.user;


import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;


@Service
public class UpdateUserPutService {

    @Autowired
    private UserRepository userRepository;

    public User updateUser(UpdateUserPutInput input) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (!role.equals("ROLE_ADMINISTRADOR") && !role.equals("ROLE_GESTOR")) {
            throw new AccessDeniedException("Você não tem permissão para atualizar usuários.");
        }

        // 🔥 2) Continua com a lógica normal
        final var existingEntity = userRepository.findById(input.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica duplicidade
        if (userRepository.existsByUsernameAndUserIdNot(input.username(), input.userId())) {
            throw new RuntimeException("Já existe um usuário com esse nome");
        }

        final var updatedUser = new User(
                existingEntity.getUserId(),
                input.username(),
                input.password(),
                input.email(),
                true,
                input.sector(),
                input.position()
        );

        final var updatedEntity = UserEntityMapper.toEntity(updatedUser);
        final var savedEntity = userRepository.save(updatedEntity);

        return UserEntityMapper.toUser(savedEntity);
    }
}

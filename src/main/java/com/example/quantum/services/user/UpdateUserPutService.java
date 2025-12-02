package com.example.quantum.services.user;

import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntityMapper;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;

@Service
public class UpdateUserPutService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User updateUser(UpdateUserPutInput input) {
        // Correção aqui: remova o .get() extra
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (!role.equals("ROLE_ADMINISTRADOR") && !role.equals("ROLE_GESTOR")) {
            throw new AccessDeniedException("Você não tem permissão para atualizar usuários.");
        }

        final var existingEntity = userRepository.findById(input.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica duplicidade
        if (userRepository.existsByUsernameAndUserIdNot(input.username(), input.userId())) {
            throw new RuntimeException("Já existe um usuário com esse nome");
        }

        // 🔥 Correção: Mantenha a senha existente se não for fornecida
        String newPassword = existingEntity.getPassword();  // Senha original (já hasheada)
        if (StringUtils.hasText(input.password())) {  // Se password não for null ou vazio
            newPassword = passwordEncoder.encode(input.password());  // Hasheie a nova senha
        }

        final var updatedUser = new User(
                existingEntity.getUserId(),
                input.username(),
                newPassword,  // Use a senha corrigida
                input.email(),
                input.active(),
                input.sector(),
                input.position()
        );

        final var updatedEntity = UserEntityMapper.toEntity(updatedUser);
        final var savedEntity = userRepository.save(updatedEntity);

        return UserEntityMapper.toUser(savedEntity);
    }
}

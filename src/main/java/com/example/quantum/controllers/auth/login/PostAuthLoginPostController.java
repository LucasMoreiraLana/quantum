package com.example.quantum.controllers.auth.login;

import com.example.quantum.infra.security.TokenService;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class PostAuthLoginPostController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody PostAuthLoginPostRequest request) {
        // Busca o usuário pelo email
        final var entity = this.userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email ou senha incorretos"));  // Mudei a mensagem para genérica por segurança

        // Verifica se o usuário está ativo
        if (!entity.isActive()) {  // Assumindo que UserEntity tem boolean isActive() ou getActive()
            throw new RuntimeException("Email ou senha incorretos");  // Mesma mensagem genérica para não revelar inatividade
        }

        // Verifica a senha
        if (passwordEncoder.matches(request.password(), entity.getPassword())) {
            final var token = tokenService.generatedToken(entity);
            return ResponseEntity.ok(new PostAuthLoginPostResponse(entity.getUsername(), token));
        }

        // Senha incorreta
        throw new RuntimeException("Email ou senha incorretos");  // Use exception em vez de badRequest para consistência com handler
    }
}
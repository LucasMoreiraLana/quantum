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
@RequestMapping("/auth)")
@RequiredArgsConstructor
public class PostAuthLoginPostController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody PostAuthLoginPostRequest request){

        UserEntity entity = this.userRepository.findByEmail(request.email()).orElseThrow(()-> new RuntimeException("O usuário não existe!"));
        if(passwordEncoder.matches(entity.getPassword(), request.password())){
            String token = this.tokenService.generatedToken(entity);
            return ResponseEntity.ok(new PostAuthLoginPostResponse(entity.getUsername(), token));
        }
        return ResponseEntity.badRequest().build();

    }
}

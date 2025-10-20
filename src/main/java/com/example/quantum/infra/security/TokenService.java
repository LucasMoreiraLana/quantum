package com.example.quantum.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.quantum.domain.User;
import com.example.quantum.repositories.user.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final static String ISSUER_NAME = "quantum";

    public String generatedToken(UUID userId){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer(ISSUER_NAME)
                    .withSubject(userId.toString())
                    .withExpiresAt(this.generationExpirationDate())
                    .sign(algorithm);

        } catch(JWTCreationException exception) {
            throw new RuntimeException("Erro de autenticação.");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER_NAME)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch(JWTVerificationException exception){
            return null;
        }
    }

    private Instant generationExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }


}

package com.example.quantum.repositories.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUserId(UUID userId);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndUserIdNot(String username, UUID userId);
    boolean existsByEmail(String email);

    
}

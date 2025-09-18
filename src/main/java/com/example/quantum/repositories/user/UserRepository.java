package com.example.quantum.repositories.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, UUID> {
    Optional<UserEntity> findByIdUser(UUID idUser);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdUserNot(String username, UUID idUser);

    
}

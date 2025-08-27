package com.example.quantum.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.UUID;
import java.util.Optional;
import com.example.quantum.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByIdUser(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    
}

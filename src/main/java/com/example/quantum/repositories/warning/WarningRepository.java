package com.example.quantum.repositories.warning;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarningRepository extends MongoRepository<WarningEntity, UUID> {
    Optional<WarningEntity> findByWarningId(UUID warningId);
    boolean existsByWarningTitle(String warningTitle);
}

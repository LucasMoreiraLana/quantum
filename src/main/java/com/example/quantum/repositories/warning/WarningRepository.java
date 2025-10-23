package com.example.quantum.repositories.warning;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface WarningRepository extends MongoRepository<WarningEntity, UUID> {
    boolean existsByWarningTitle(String warningTitle);
}

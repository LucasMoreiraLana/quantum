package com.example.quantum.repositories.identifywarning;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface WarningRepository extends MongoRepository<WarningEntity, UUID> {



}

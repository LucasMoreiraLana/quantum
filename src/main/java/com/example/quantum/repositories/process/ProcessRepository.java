package com.example.quantum.repositories.process;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessRepository extends MongoRepository<ProcessEntity, UUID> {
}

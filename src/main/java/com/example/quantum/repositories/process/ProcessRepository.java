package com.example.quantum.repositories.process;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ProcessRepository extends MongoRepository<ProcessEntity, UUID> {
}

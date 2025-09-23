package com.example.quantum.repositories.action;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ActionRepository extends MongoRepository<ActionEntity, UUID> {
}

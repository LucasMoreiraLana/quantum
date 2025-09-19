package com.example.quantum.repositories.noncompliance;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface NonComplianceRepository extends MongoRepository<NonComplianceEntity, UUID> {


}

package com.example.quantum.repositories.indicators;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface IndicatorsRepository extends MongoRepository<IndicatorEntity, UUID>{

}

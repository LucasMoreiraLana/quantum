package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.NonCompliance;

import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertNonCompliancePostService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    public NonCompliance createNonCompliance(InsertNonCompliancePostInput input){

        final var nonCompliance = input.toDomain();

        final var entity = NonComplianceEntityMapper.toEntity(nonCompliance);

        final var savedEntity = nonComplianceRepository.save(entity);

        return NonComplianceEntityMapper.toNonCompliance(savedEntity);
    }
}

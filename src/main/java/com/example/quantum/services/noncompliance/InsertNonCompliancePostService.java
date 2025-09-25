package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertNonCompliancePostService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    @Autowired
    private ProcessRepository processRepository;

    public NonCompliance createNonCompliance(InsertNonCompliancePostInput input) {


       final var nonCompliance = input.toDomain();

       final var entity = NonComplianceEntityMapper.toEntity(nonCompliance);

       if(processRepository.existsById(input.ProcessId())){
           throw new IllegalArgumentException("O processo informado não existe!");
       }

       final var savedEntity = nonComplianceRepository.save(entity);

        return NonComplianceEntityMapper.toNonCompliance(savedEntity);
    }
}

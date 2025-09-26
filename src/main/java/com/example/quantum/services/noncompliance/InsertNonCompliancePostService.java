package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import com.example.quantum.repositories.process.ProcessRepository;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertNonCompliancePostService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private UserRepository userRepository;

    public NonCompliance createNonCompliance(InsertNonCompliancePostInput input) {

        // 1. Validações
        if (!processRepository.existsById(input.ProcessId())) {
            throw new IllegalArgumentException("O processo informado não existe!");
        }

        if (!userRepository.existsById(input.createdBy())) {
            throw new IllegalArgumentException("O usuário informado não existe!");
        }

        // 2. Converte o input para o domínio
        final var nonCompliance = input.toDomain();

        // 3. Converte para entity
        final var entity = NonComplianceEntityMapper.toEntity(nonCompliance);

        // 4. Salva
        final var savedEntity = nonComplianceRepository.save(entity);

        // 5. Retorna para o domínio
        return NonComplianceEntityMapper.toNonCompliance(savedEntity);
    }
}



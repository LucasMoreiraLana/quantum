package com.example.quantum.services.noncompliance;


import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import com.example.quantum.repositories.process.ProcessRepository;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InsertNonCompliancePostService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private UserRepository userRepository;

    public InserNonComplianceServicePostOutput createNonCompliance(InsertNonCompliancePostInput input) {

        boolean processExists = processRepository.existsById(input.processId()); // Nota: 'ProcessId' com P maiúsculo conforme seu arquivo Input
        if (!processExists) {
            throw new RuntimeException("Não é possível criar a Não Conformidade: O Processo selecionado não existe.");
        }

        final var nonCompliance = input.toDomain();
        final var entity = NonComplianceEntityMapper.toEntity(nonCompliance);
        final var savedNonComplianceEntity = nonComplianceRepository.save(entity); // Retorna Entidade

        // --- PASSO CRÍTICO: CONVERTER ENTIDADE PARA DOMÍNIO ---
        final var savedNonComplianceDomain = NonComplianceEntityMapper.toNonCompliance(savedNonComplianceEntity);

        final UUID createdBy = savedNonComplianceEntity.getCreatedBy(); // Usa a Entidade para buscar o ID

        final String createdByName = userRepository.findById(createdBy)
                .map(UserEntity::getUsername)
                .orElse("Usuário Desconhecido");

        // --- USA O OBJETO DE DOMÍNIO AQUI ---
        return new InserNonComplianceServicePostOutput(savedNonComplianceDomain, createdByName);
    }
}



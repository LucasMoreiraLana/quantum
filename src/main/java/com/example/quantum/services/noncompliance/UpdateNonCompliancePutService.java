package com.example.quantum.services.noncompliance;


import com.example.quantum.domain.NonCompliance;
import com.example.quantum.domain.Process;
import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateNonCompliancePutService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    @Autowired
    private ProcessRepository processRepository;

    public NonCompliance updateNonCompliance(UpdateNonCompliancePutInput input){
        final var existingNonCompliance = nonComplianceRepository.findById(input.nonComplianceId())
                .orElseThrow(() -> new RuntimeException("Não-Conformidade não encontrada."));

        final var existingProcess = processRepository.findById(input.processId())
                .orElseThrow(() -> new RuntimeException("Processo não encontrado."));

        final var updatedDomain = new NonCompliance(
                input.nonComplianceId(),
                existingNonCompliance.getCreatedBy(),
                input.dateOpening(),
                existingProcess.getProcessId(),
                input.sector(),
                input.origin(),
                input.priority(),
                input.customer(),
                input.description(),
                input.efficacy(),
                input.datePrevision()

        );

        final var updatedEntity = NonComplianceEntityMapper.toEntity(updatedDomain);

        final var savedEntity = nonComplianceRepository.save(updatedEntity);

        return NonComplianceEntityMapper.toNonCompliance(savedEntity);
    }
}

package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntityMapper;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProcessPutService {

    @Autowired
    private ProcessRepository processRepository;

    public com.example.quantum.domain.Process updateProcess(UpdateProcessPutInput input){

        final var existingEntity = processRepository.findById(input.processId())
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));


        final var updateProcess = new Process(
                existingEntity.getProcessId(),
                input.createdBy(),
                input.nameProcess(),
                input.dateApproval(),
                input.dateConclusion(),
                input.sector(),
                input.cyclePDCA()
        );

        final var updatedEntity = ProcessEntityMapper.toEntity(updateProcess);


        final var savedEntity = processRepository.save(updatedEntity);

        return ProcessEntityMapper.toProcess(savedEntity);

    }
}

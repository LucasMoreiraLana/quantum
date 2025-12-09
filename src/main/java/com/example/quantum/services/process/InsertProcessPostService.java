package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntityMapper;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InsertProcessPostService {

    @Autowired
    private ProcessRepository processRepository;

    public Process insertProcess(InsertProcessPostInput input){
        // O input.toDomain() (se estiver como no exemplo acima)
        // já usará o createdBy injetado para criar a entidade Process.
        final var process = input.toDomain();

        final var entity = ProcessEntityMapper.toEntity(process);

        if (processRepository.existsByNameProcess(process.nameProcess())) {
            throw new IllegalArgumentException("Já existe um Processo com esse nome!");
        }

        final var savedEntity = processRepository.save(entity);

        return ProcessEntityMapper.toProcess(savedEntity);
    }
}

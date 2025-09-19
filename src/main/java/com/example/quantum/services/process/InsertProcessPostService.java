package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntityMapper;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertProcessPostService {

    @Autowired
    private ProcessRepository processRepository;

    public Process create(InsertProcessPostInput input){

        //input -> domain
        final var process = input.toDomain();

        //domain -> entity
        final var entity = ProcessEntityMapper.toEntity(process);

        if (processRepository.existsByNameProcess(process.nameProcess())) {
            throw new IllegalArgumentException("Já existe um processo com esse nome!");
        }

        //salvar
        final var savedEntity = processRepository.save(entity);
        System.out.println(process.idProcess());
        return ProcessEntityMapper.toProcess(savedEntity);

    }

}

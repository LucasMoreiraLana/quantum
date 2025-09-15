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

        //salvar
        final var savedEntity = processRepository.save(entity);

        return ProcessEntityMapper.toProcess(savedEntity);
    }

}

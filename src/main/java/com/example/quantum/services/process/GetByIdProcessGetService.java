package com.example.quantum.services.process;



import com.example.quantum.controllers.process.getbyidprocess.GetByIdProcessGetMapper;
import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByIdProcessGetService {

    @Autowired
    private final ProcessRepository processRepository;

    public GetByIdProcessGetService(ProcessRepository processRepository){
        this.processRepository = processRepository;
    }

    public Optional<Process> execute(GetByIdProcessGetInput input){
        return processRepository.findById(input.idProcess())
                .map(GetByIdProcessGetMapper::toDomain);
    }

}

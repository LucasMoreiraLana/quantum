package com.example.quantum.services.process;



import com.example.quantum.controllers.process.getbyidprocess.GetByProcessIdGetMapper;
import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetByProcessIdGetService {

    @Autowired
    private ProcessRepository processRepository;

    public Optional<Process> execute(GetByProcessIdGetInput input){
        return processRepository.findById(input.processId())
                .map(GetByProcessIdGetMapper::toDomain);
    }

}

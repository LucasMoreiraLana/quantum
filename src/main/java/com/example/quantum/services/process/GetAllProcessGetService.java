package com.example.quantum.services.process;


import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntityMapper;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllProcessGetService {

    @Autowired
    private ProcessRepository processRepository;

    public List<Process> getAllProcess(){
        return processRepository.findAll()
                .stream()
                .map(ProcessEntityMapper::toProcess)//entity -> domain
                .toList();
    }
}

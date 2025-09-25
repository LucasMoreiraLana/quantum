package com.example.quantum.services.process;

import com.example.quantum.exceptions.ProcessNotFoundException;
import com.example.quantum.repositories.process.ProcessEntity;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteByProcessIdDeleteService {

    @Autowired
    private ProcessRepository processRepository;

    public void deleteProcess(UUID processId){
        ProcessEntity processEntity = processRepository.findById(processId)
                .orElseThrow(() -> new ProcessNotFoundException("Processo não encontrado" + processId));
    }
}

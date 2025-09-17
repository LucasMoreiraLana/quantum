package com.example.quantum.services.process;

import com.example.quantum.exceptions.ProcessNotFoundException;
import com.example.quantum.repositories.process.ProcessEntity;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteByIdProcessDeleteService {

    @Autowired
    private ProcessRepository processRepository;

    public void deleteProcess(UUID idProcess){
        ProcessEntity processEntity = processRepository.findById(idProcess)
                .orElseThrow(() -> new ProcessNotFoundException("Processo não encontrado" + idProcess));
    }
}

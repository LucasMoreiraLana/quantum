package com.example.quantum.services.noncompliance;

import com.example.quantum.domain.Action;
import com.example.quantum.domain.NonCompliance;

import com.example.quantum.repositories.action.ActionRepository;
import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertNonCompliancePostService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ActionRepository actionRepository;

    public NonCompliance createNonCompliance(InsertNonCompliancePostInput input){

        var nonCompliance = input.toDomain();

        // Resolver Process
        var process = processRepository.findById(input.idProcess())
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));

        nonCompliance.setProcess(new Process(process.getIdProcess(), process.getNameProcess()));

        // Resolver Action
        var action = actionRepository.findById(input.idAction())
                .orElseThrow(() -> new RuntimeException("Ação não encontrada"));

        nonCompliance.setAction(new Action(action.getIdAction(), action.getNameAction()));

        // Salvar
        var entity = NonComplianceEntityMapper.toEntity(nonCompliance);
        var savedEntity = nonComplianceRepository.save(entity);

        return NonComplianceEntityMapper.toNonCompliance(savedEntity);
    }
}

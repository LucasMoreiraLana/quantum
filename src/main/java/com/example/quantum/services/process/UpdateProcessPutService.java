package com.example.quantum.services.process;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntity;
import com.example.quantum.repositories.process.ProcessEntityMapper;
import com.example.quantum.repositories.process.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateProcessPutService {

    @Autowired
    private ProcessRepository processRepository;

    public Process updateProcess(UpdateProcessPutInput input) {
        // 1. BUSCAR O PROCESSO EXISTENTE
        ProcessEntity existingEntity = processRepository.findById(input.processId())
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));

        // 2. PRESERVAR O createdBy ORIGINAL
        UUID originalCreatedBy = existingEntity.getCreatedBy();

        // 3. ATUALIZAR APENAS OS CAMPOS EDITÁVEIS
        existingEntity.setNameProcess(input.nameProcess());
        existingEntity.setDateApproval(input.dateApproval());
        existingEntity.setDateConclusion(input.dateConclusion());
        existingEntity.setSector(input.sector());
        existingEntity.setCyclePDCA(input.cyclePDCA());
        // ✅ NÃO atualiza o createdBy

        // 4. SALVAR
        ProcessEntity updatedEntity = processRepository.save(existingEntity);

        // 5. RETORNAR DOMAIN COM createdBy PRESERVADO
        return new Process(
                updatedEntity.getProcessId(),
                originalCreatedBy,  // ✅ Mantém o criador original
                updatedEntity.getNameProcess(),
                updatedEntity.getDateApproval(),
                updatedEntity.getDateConclusion(),
                updatedEntity.getSector(),
                updatedEntity.getCyclePDCA()
        );
    }
}

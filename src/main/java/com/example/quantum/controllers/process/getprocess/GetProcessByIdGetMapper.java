package com.example.quantum.controllers.process.getprocess;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessEntity;
import com.example.quantum.services.process.GetProcessServiceGetOutput;


public class GetProcessByIdGetMapper {

    // 1. MÉTODO DE RESPOSTA CORRIGIDO (Recebe o Output do Service)
    public static GetProcessByIdGetResponse toResponse(GetProcessServiceGetOutput output) {
        final Process process = output.process();

        // Obtém o nome ou usa o fallback
        final String createdByName = output.createdByName().orElse("Usuário Desconhecido");

        return new GetProcessByIdGetResponse(
                process.processId(),
                process.createdBy(),
                process.nameProcess(),
                process.dateApproval(),
                process.dateConclusion(),
                process.sector(),
                process.cyclePDCA(),

                // NOVO CAMPO: Mapeia o nome
                createdByName
        );
    }

    // 2. Converte de Entity (banco) para Domain (negócio) - Permanece o mesmo
    public static Process toDomain(ProcessEntity entity) {
        if (entity == null) return null;

        return new Process(
                entity.getProcessId(),
                entity.getCreatedBy(),
                entity.getNameProcess(),
                entity.getDateApproval(),
                entity.getDateConclusion(),
                entity.getSector(),
                entity.getCyclePDCA()
        );
    }

}
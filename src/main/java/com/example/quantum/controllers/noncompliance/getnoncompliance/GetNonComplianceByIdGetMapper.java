package com.example.quantum.controllers.noncompliance.getnoncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.repositories.noncompliance.NonComplianceEntity;
import com.example.quantum.services.noncompliance.GetNonComplianceServiceGetOutput; // NOVO IMPORT!

public class GetNonComplianceByIdGetMapper {

    // MÉTODO CORRIGIDO: Recebe o DTO de Saída do Service
    public static GetNonComplianceByIdGetResponse toResponse(GetNonComplianceServiceGetOutput output){

        // 1. Extrair o Objeto de Domínio e o Nome do Criador do DTO de Saída
        final NonCompliance nonCompliance = output.nonCompliance();

        // O createdByName está em um Optional. Usamos orElse para garantir um valor, se não for encontrado.
        final String createdByName = output.createdByName().orElse("Usuário Desconhecido");

        return new GetNonComplianceByIdGetResponse(
                nonCompliance.nonComplianceId(),
                nonCompliance.createdBy(),
                nonCompliance.dateOpening(),
                nonCompliance.processId(),
                nonCompliance.sector(),
                nonCompliance.origin(),
                nonCompliance.priority(),
                nonCompliance.customer(),
                nonCompliance.description(),
                nonCompliance.efficacy(),
                nonCompliance.datePrevision(),
                // NOVO CAMPO: Adiciona o nome do criador
                createdByName
        );
    }

    // O método toDomain permanece o mesmo, pois faz a conversão de Entidade para Domínio
    public static NonCompliance toDomain(NonComplianceEntity entity){
        if(entity == null) return null;

        return new NonCompliance(
                entity.getNonComplianceId(),
                entity.getCreatedBy(),
                entity.getDateOpening(),
                entity.getProcessId(),
                entity.getSector(),
                entity.getOrigin(),
                entity.getPriority(),
                entity.getCustomer(),
                entity.getDescription(),
                entity.isEfficacy(),
                entity.getDatePrevision()
        );
    }
}
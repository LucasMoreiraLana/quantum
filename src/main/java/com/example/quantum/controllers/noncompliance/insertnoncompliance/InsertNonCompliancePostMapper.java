package com.example.quantum.controllers.noncompliance.insertnoncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.services.noncompliance.InsertNonCompliancePostInput;
import com.example.quantum.services.noncompliance.InserNonComplianceServicePostOutput;
import java.util.UUID; // IMPORT NECESSÁRIO!

public class InsertNonCompliancePostMapper {

    public static InsertNonCompliancePostInput toInput(InsertNonCompliancePostRequest request){

        // CORREÇÃO: Gerar o ID único para a nova Inconformidade
        final UUID nonComplianceId = UUID.randomUUID();

        return new InsertNonCompliancePostInput(
                nonComplianceId, // 1. O ID GERADO É O PRIMEIRO ARGUMENTO
                request.createdBy(),
                request.dateOpening(),
                request.processId(),
                request.sector(),
                request.origin(),
                request.priority(),
                request.customer(),
                request.description(),
                request.efficacy(),
                request.datePrevision()
        );
    }

    // Mantido o método toResponse corrigido anteriormente para o DTO de Saída
    public static InsertNonCompliancePostResponse toResponse(InserNonComplianceServicePostOutput output){

        final NonCompliance nonCompliance = output.nonCompliance();
        final String createdByName = output.createdByName();

        return new InsertNonCompliancePostResponse(
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
                createdByName
        );
    }
}
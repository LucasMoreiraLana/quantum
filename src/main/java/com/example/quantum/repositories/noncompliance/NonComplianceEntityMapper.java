package com.example.quantum.repositories.noncompliance;

import com.example.quantum.domain.NonCompliance;

public class NonComplianceEntityMapper {

    public static NonCompliance toNonCompliance(NonComplianceEntity entity){
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
                // CORRIGIDO: Usando getDatePrevision()
                entity.getDatePrevision()
        );
    }

    public static NonComplianceEntity toEntity(NonCompliance nonCompliance){
        NonComplianceEntity entity =  new NonComplianceEntity();
        entity.setNonComplianceId(nonCompliance.nonComplianceId());
        entity.setCreatedBy(nonCompliance.createdBy());
        entity.setProcessId(nonCompliance.processId());
        entity.setDateOpening(nonCompliance.dateOpening());
        entity.setSector(nonCompliance.sector());
        entity.setOrigin(nonCompliance.origin());
        entity.setPriority(nonCompliance.priority());
        entity.setCustomer(nonCompliance.customer());
        entity.setDescription(nonCompliance.description());
        entity.setEfficacy(nonCompliance.efficacy());

        // CORRIGIDO: Usando setDatePrevision()
        entity.setDatePrevision(nonCompliance.datePrevision());
        return entity;
    }
}
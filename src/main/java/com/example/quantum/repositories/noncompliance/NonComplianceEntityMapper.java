package com.example.quantum.repositories.noncompliance;

import com.example.quantum.domain.NonCompliance;

public class NonComplianceEntityMapper {

    public static NonCompliance toNonCompliance(NonComplianceEntity entity){

        return new NonCompliance(
                  entity.getIdNonCompliance(),
                  entity.getCreatedBy(),
                entity.getDateOpening(),
                entity.getNameProcess(),
                  entity.getSector(),
                  entity.getOrigin(),
                  entity.getPriority(),
                  entity.getCustomer(),
                  entity.getAction(),
                  entity.getDescription(),
                  entity.isEfficacy(),
                  entity.getDataPrevision()
                );
    }

    public static NonComplianceEntity toEntity(NonCompliance nonCompliance){
        NonComplianceEntity entity =  new NonComplianceEntity();
        entity.setIdNonCompliance(nonCompliance.idNonCompliance());
        entity.setCreatedBy(nonCompliance.createdBy());
        entity.setNameProcess(nonCompliance.nameProcess());
        entity.setDateOpening(nonCompliance.dateOpening());
        entity.setSector(nonCompliance.sector());
        entity.setOrigin(nonCompliance.origin());
        entity.setPriority(nonCompliance.priority());
        entity.setCustomer(nonCompliance.customer());
        entity.setAction(nonCompliance.action());
        entity.setDescription(nonCompliance.description());
        entity.setEfficacy(nonCompliance.efficacy());
        entity.setDataPrevision(nonCompliance.datePrevision());
        return entity;
    }
}

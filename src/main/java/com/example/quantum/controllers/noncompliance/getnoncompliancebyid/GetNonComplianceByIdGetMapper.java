package com.example.quantum.controllers.noncompliance.getnoncompliancebyid;

import com.example.quantum.controllers.noncompliance.getnoncompliance.GetAllNonComplianceGetResponse;
import com.example.quantum.domain.NonCompliance;
import com.example.quantum.repositories.noncompliance.NonComplianceEntity;

public class GetNonComplianceByIdGetMapper {

    public static GetNonComplianceByIdGetResponse toResponse(NonCompliance nonCompliance){
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
                nonCompliance.datePrevision()
        );
    }

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
                entity.getDataPrevision()
        );
    }
}

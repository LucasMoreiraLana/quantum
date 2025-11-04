package com.example.quantum.controllers.noncompliance.updatenoncompliance;

import com.example.quantum.services.noncompliance.UpdateNonCompliancePutInput;
import com.example.quantum.domain.NonCompliance;
import java.util.UUID;

public class UpdateNonCompliancePutMapper {


    public static UpdateNonCompliancePutInput toNonComplianceInput(UUID ncId, UpdateNonCompliancePutRequest request){
        return new UpdateNonCompliancePutInput(
                ncId,
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

    public static UpdateNonCompliancePutResponse toNonComplianceResponse(NonCompliance nonCompliance){
        return new UpdateNonCompliancePutResponse(
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
}

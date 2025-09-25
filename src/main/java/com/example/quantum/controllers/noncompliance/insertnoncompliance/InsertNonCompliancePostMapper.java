package com.example.quantum.controllers.noncompliance.insertnoncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.services.noncompliance.InsertNonCompliancePostInput;

public class InsertNonCompliancePostMapper {

    public static InsertNonCompliancePostInput toInput(InsertNonCompliancePostRequest request){
        return new InsertNonCompliancePostInput(

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

    public static InsertNonCompliancePostResponse toResponse(NonCompliance nonCompliance){

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
                nonCompliance.datePrevision()
        );
    }
}

package com.example.quantum.controllers.noncompliance.insertnoncompliance;

import com.example.quantum.domain.NonCompliance;
import com.example.quantum.services.noncompliance.InsertNonCompliancePostInput;
import com.example.quantum.domain.Process;


import java.util.UUID;

public class InsertNonCompliancePostMapper {

    public static InsertNonCompliancePostInput toInput(InsertNonCompliancePostRequest request){
        return new InsertNonCompliancePostInput(

                request.createdBy(),
                request.dateOpening(),
                request.nameProcess(),
                request.sector(),
                request.origin(),
                request.priority(),
                request.customer(),
                request.action(),
                request.description(),
                request.efficacy(),
                request.datePrevision()


        );
    }

    public static InsertNonCompliancePostResponse toResponse(NonCompliance nonCompliance){

        return new InsertNonCompliancePostResponse(
                nonCompliance.idNonCompliance(),
                nonCompliance.createdBy(),
                nonCompliance.dateOpening(),
                nonCompliance.nameProcess(),
                nonCompliance.sector(),
                nonCompliance.origin(),
                nonCompliance.priority(),
                nonCompliance.customer(),
                nonCompliance.action(),
                nonCompliance.description(),
                nonCompliance.efficacy(),
                nonCompliance.datePrevision()
        );
    }
}

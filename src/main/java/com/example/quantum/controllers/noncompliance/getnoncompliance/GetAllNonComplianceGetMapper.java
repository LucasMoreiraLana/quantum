package com.example.quantum.controllers.noncompliance.getnoncompliance;

import com.example.quantum.domain.NonCompliance;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllNonComplianceGetMapper{

    public static GetAllNonComplianceGetResponse toResponse (NonCompliance nonCompliance){
        return new GetAllNonComplianceGetResponse(
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

    public static List<GetAllNonComplianceGetResponse> toResponseList(List<NonCompliance> nonCompliances){
        return nonCompliances.stream()
                .map(GetAllNonComplianceGetMapper::toResponse)
                .collect(Collectors.toList());
    }

}

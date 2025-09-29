package com.example.quantum.controllers.noncompliance.getnoncompliancebyid;


import com.example.quantum.services.noncompliance.GetNonComplianceByIdGetInput;
import com.example.quantum.services.noncompliance.GetNonComplianceByIdGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/v1/non-compliance")
public class GetNonComplianceByIdGetController {

    @Autowired
    private GetNonComplianceByIdGetService getNonComplianceByIdGetService;

    @GetMapping("/{non-complianceId}")
    public ResponseEntity<GetNonComplianceByIdGetResponse> getNonComplianceByIdResponse (@PathVariable UUID nonComplianceId){

        GetNonComplianceByIdGetInput input = new GetNonComplianceByIdGetInput(nonComplianceId);

        return getNonComplianceByIdGetService.execute(input)
                .map(nonCompliance -> ResponseEntity.ok(GetNonComplianceByIdGetMapper.toResponse(nonCompliance)))
                .orElse(ResponseEntity.notFound().build());
    }
}

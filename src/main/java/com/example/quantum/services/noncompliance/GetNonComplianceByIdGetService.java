package com.example.quantum.services.noncompliance;

import com.example.quantum.controllers.noncompliance.getnoncompliance.GetNonComplianceByIdGetMapper;
import com.example.quantum.domain.NonCompliance;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetNonComplianceByIdGetService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    public Optional<NonCompliance> execute(GetNonComplianceByIdGetInput input){
        return nonComplianceRepository
                .findById(input.nonComplianceId())
                .map(GetNonComplianceByIdGetMapper::toDomain);
    }
}

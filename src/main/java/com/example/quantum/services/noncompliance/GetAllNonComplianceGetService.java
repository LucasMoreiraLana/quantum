package com.example.quantum.services.noncompliance;


import com.example.quantum.domain.NonCompliance;
import com.example.quantum.repositories.noncompliance.NonComplianceEntityMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllNonComplianceGetService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    public List<NonCompliance> getAllNonCompliance(){

        return nonComplianceRepository.findAll()
                .stream()
                .map(NonComplianceEntityMapper::toNonCompliance)
                .toList();
    }
}

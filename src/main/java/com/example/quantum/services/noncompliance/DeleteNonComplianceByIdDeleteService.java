package com.example.quantum.services.noncompliance;


import com.example.quantum.exceptions.NonComplianceNotFoundException;
import com.example.quantum.repositories.noncompliance.NonComplianceEntity;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteNonComplianceByIdDeleteService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    public void deleteNonCompliance(UUID nonComplianceId){
        NonComplianceEntity nonComplianceEntity = nonComplianceRepository.findById(nonComplianceId)
                .orElseThrow(()-> new NonComplianceNotFoundException("Não-Conformidade não encontrada: " + nonComplianceId));
        nonComplianceRepository.delete(nonComplianceEntity);
    }
}

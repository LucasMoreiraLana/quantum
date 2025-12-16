package com.example.quantum.services.noncompliance;

import com.example.quantum.controllers.noncompliance.getnoncompliance.GetNonComplianceByIdGetMapper;
import com.example.quantum.repositories.noncompliance.NonComplianceRepository;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetNonComplianceByIdGetService {

    @Autowired
    private NonComplianceRepository nonComplianceRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<GetNonComplianceServiceGetOutput> execute(GetNonComplianceByIdGetInput input){

        return nonComplianceRepository.findById(input.nonComplianceId())
                .map(GetNonComplianceByIdGetMapper::toDomain)
                .map(nonCompliance -> {

                    final var createdBy = nonCompliance.createdBy();

                    final Optional<String> createdByName = userRepository.findById(createdBy)
                            .map(UserEntity::getUsername);

                    return new GetNonComplianceServiceGetOutput(nonCompliance, createdByName);

                });
    }
}

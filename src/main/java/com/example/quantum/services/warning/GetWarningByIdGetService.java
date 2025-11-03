package com.example.quantum.services.warning;


import com.example.quantum.controllers.warning.getwarning.GetAllWarningGetMapper;
import com.example.quantum.controllers.warning.getwarning.GetWarningByIdGetMapper;
import com.example.quantum.domain.Warning;
import com.example.quantum.repositories.warning.WarningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetWarningByIdGetService {


    @Autowired
    private WarningRepository warningRepository;

    public Optional<Warning> execute(GetWarningByIdGetInput input){
        return warningRepository.findByWarningId(input.warningId())
                .map(GetWarningByIdGetMapper::toDomain);
    }
}

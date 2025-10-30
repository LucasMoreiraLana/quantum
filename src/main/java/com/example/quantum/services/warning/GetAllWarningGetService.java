package com.example.quantum.services.warning;


import com.example.quantum.domain.Warning;
import com.example.quantum.repositories.warning.WarningEntity;
import com.example.quantum.repositories.warning.WarningEntityMapper;
import com.example.quantum.repositories.warning.WarningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAllWarningGetService {

    @Autowired
    private WarningRepository warningRepository;

    public List<Warning> getAllWarning(){

        return warningRepository.findAll()
                .stream()
                .map(WarningEntityMapper::toWarning)
                .collect(Collectors.toList());
    }

}

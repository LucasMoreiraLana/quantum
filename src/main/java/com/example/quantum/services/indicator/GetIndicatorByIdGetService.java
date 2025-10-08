package com.example.quantum.services.indicator;


import com.example.quantum.controllers.indicator.getindicatorbyid.GetIndicatorByIdGetMapper;
import com.example.quantum.domain.Indicator;
import com.example.quantum.repositories.indicators.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetIndicatorByIdGetService {

    @Autowired
    private IndicatorRepository indicatorRepository;

    public Optional<Indicator> execute(GetIndicatorByIdGetInput input){
        return indicatorRepository.findById(input.indicatorId())
                .map(GetIndicatorByIdGetMapper::toDomain);
    }
}

package com.example.quantum.services.indocator;


import com.example.quantum.domain.Indicator;
import com.example.quantum.repositories.indicators.IndicatorEntityMapper;
import com.example.quantum.repositories.indicators.IndicatorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllIndicatorGetService {

    @Autowired
    private IndicatorsRepository indicatorsRepository;

    public List<Indicator> getAllIndicator(){
        return indicatorsRepository.findAll()
                .stream()
                .map(IndicatorEntityMapper::toIndicator)
                .toList();
    }

}

package com.example.quantum.services.indicator;


import com.example.quantum.domain.Indicator;
import com.example.quantum.repositories.indicators.IndicatorEntityMapper;
import com.example.quantum.repositories.indicators.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllIndicatorGetService {

    @Autowired
    private IndicatorRepository indicatorRepository;

    public List<Indicator> getAllIndicator(){
        return indicatorRepository.findAll()
                .stream()
                .map(IndicatorEntityMapper::toIndicator)
                .toList();
    }

}

package com.example.quantum.services.indicator;


import com.example.quantum.exceptions.IndicatorNotFoundException;
import com.example.quantum.repositories.indicators.IndicatorEntity;
import com.example.quantum.repositories.indicators.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteIndicatorByIdDeleteService {

    @Autowired
    private IndicatorRepository indicatorRepository;

    public void deleteIndicator(UUID indicatorId){
        IndicatorEntity indicatorEntity = indicatorRepository.findById(indicatorId)
                .orElseThrow(()-> new IndicatorNotFoundException("Indicador não encontrado: " + indicatorId));
        indicatorRepository.save(indicatorEntity);
    }
}

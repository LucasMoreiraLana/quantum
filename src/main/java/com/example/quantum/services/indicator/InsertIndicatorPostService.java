package com.example.quantum.services.indicator;


import com.example.quantum.domain.Indicator;
import com.example.quantum.repositories.indicators.IndicatorEntityMapper;
import com.example.quantum.repositories.indicators.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertIndicatorPostService {

    @Autowired
    private IndicatorRepository indicatorRepository;

    public Indicator createIndicator(InsertIndicatorPostInput input){

        final var indicator = input.toDomain();

        final var entity = IndicatorEntityMapper.toEntity(indicator);

        if(indicatorRepository.existsByNameIndicator(indicator.nameIndicator())){
            throw new IllegalArgumentException("Já existe um indicador com este nome.");
        }

        final var savedEntity = indicatorRepository.save(entity);

        return IndicatorEntityMapper.toIndicator(savedEntity);

    }
}

package com.example.quantum.services.indicator;


import com.example.quantum.domain.Indicator;
import com.example.quantum.repositories.indicators.IndicatorEntityMapper;
import com.example.quantum.repositories.indicators.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateIndicatorPutService {

    @Autowired
    private IndicatorRepository indicatorRepository;

    public Indicator updateIndicator(UpdateIndicatorPutInput input) {
        final var existingEntity = indicatorRepository.findById(input.indicatorId())
                .orElseThrow(() -> new RuntimeException("Indicador não encontrado!"));


        final var updateDomain = new Indicator(
                input.indicatorId(),
                input.nameIndicator(),
                input.measurementCriterion(),
                input.sector(),
                existingEntity.getCreatedBy(),
                input.unitOfMeasurement(),
                input.frequencyOfMeasurement(),
                input.objective(),
                input.tendency(),
                input.lastDate(),
                input.nextMeasurement(),
                input.classification()
        );

        final var updatedEntity = IndicatorEntityMapper.toEntity(updateDomain);

        final var savedEntity = indicatorRepository.save(updatedEntity);

        return IndicatorEntityMapper.toIndicator(savedEntity);
    }


}

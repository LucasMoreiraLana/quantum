package com.example.quantum.repositories.indicators;

import com.example.quantum.domain.Indicator;

public class IndicatorEntityMapper {

    public static Indicator toIndicator(IndicatorEntity entity){
        return new Indicator(
                entity.getIndicatorId(),
                entity.getNameIndicator(),
                entity.getMeasurementCriterion(),
                entity.getSector(),
                entity.getCreatedBy(),
                entity.getUnitOfMeasurement(),
                entity.getFrequencyOfMeasurement(),
                entity.getObjective(),
                true,
                entity.getLastDate(),
                entity.getNextMeasurement(),
                entity.getClassification()
        );
    }

    public static IndicatorEntity toEntity(Indicator indicator){
        IndicatorEntity entity = new IndicatorEntity();
        entity.setIndicatorId(indicator.indicatorId());
        entity.setNameIndicator(indicator.nameIndicator());
        entity.setMeasurementCriterion(indicator.measurementCriterion());
        entity.setSector(indicator.sector());
        entity.setCreatedBy(indicator.createdBy());
        entity.setUnitOfMeasurement(indicator.unitOfMeasurement());
        entity.setFrequencyOfMeasurement(indicator.frequencyOfMeasurement());
        entity.setObjective(indicator.objective());
        entity.setTendency(indicator.tendency());
        entity.setLastDate(indicator.lastDate());
        entity.setNextMeasurement(indicator.nextMeasurement());
        entity.setClassification(indicator.classification());
        return entity;
    }
}

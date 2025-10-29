package com.example.quantum.controllers.indicator.getindicator;

import com.example.quantum.domain.Indicator;
import com.example.quantum.repositories.indicators.IndicatorEntity;

public class GetIndicatorByIdGetMapper {

    public static GetIndicatorByIdGetResponse toResponse(Indicator indicator){
        return new GetIndicatorByIdGetResponse(
                indicator.indicatorId(),
                indicator.nameIndicator(),
                indicator.measurementCriterion(),
                indicator.sector(),
                indicator.createdBy(),
                indicator.unitOfMeasurement(),
                indicator.frequencyOfMeasurement(),
                indicator.objective(),
                true,
                indicator.lastDate(),
                indicator.nextMeasurement(),
                indicator.classification()
        );
    }

    public static Indicator toDomain(IndicatorEntity entity){
        if(entity == null) return null;

        return new Indicator(
                entity.getIndicatorId(),
                entity.getNameIndicator(),
                entity.getMeasurementCriterion(),
                entity.getSector(),
                entity.getCreatedBy(),
                entity.getUnitOfMeasurement(),
                entity.getFrequencyOfMeasurement(),
                entity.getObjective(),
                entity.isTendency(),
                entity.getLastDate(),
                entity.getNextMeasurement(),
                entity.getClassification()
        );
    }
}

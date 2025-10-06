package com.example.quantum.services.indicator;

import com.example.quantum.domain.Indicator;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertIndicatorPostInput(

        String nameIndicator,
        String measurementCriterion,
        Sector sector,
        UUID createdBy,
        Indicator.UnitOfMeasurement unitOfMeasurement,
        Indicator.FrequencyOfMeasurement frequencyOfMeasurement,
        String objective,
        boolean tendency,
        LocalDate lastDate,
        LocalDate nextMeasurement,
        Indicator.Classification classification

) {

    public Indicator toDomain(){
        return new Indicator(
                UUID.randomUUID(),
                this.nameIndicator,
                this.measurementCriterion,
                this.sector,
                this.createdBy,
                this.unitOfMeasurement,
                this.frequencyOfMeasurement,
                this.objective,
                this.tendency,
                this.lastDate,
                this.nextMeasurement,
                this.classification

        );
    }
}

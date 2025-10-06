package com.example.quantum.controllers.indicator.insertindicator;

import com.example.quantum.domain.Indicator;
import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record InsertIndicatorPostResponse(

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

) {}

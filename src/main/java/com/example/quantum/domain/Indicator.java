package com.example.quantum.domain;

import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record Indicator(

        UUID indicatorId,
        String nameIndicator,
        String measurementCriterion,
        Sector sector,
        UUID createdBy,
        UnitOfMeasurement unitOfMeasurement,
        FrequencyOfMeasurement frequencyOfMeasurement,
        String objective,
        boolean tendency,
        LocalDate lastDate,
        LocalDate nextMeasurement,
        Classification classification

) {

    public enum UnitOfMeasurement {
        HORA,
        MOEDA,
        PERCENTUAL,
        UNIDADE
    }

    public enum FrequencyOfMeasurement {
        MENSAL,
        BIMESTRAL,
        TRIMESTRAL,
        SEMESTRAL,
        ANUAL
    }

    public enum Classification {
        RUIM,
        REGULAR,
        BOM
    }

}

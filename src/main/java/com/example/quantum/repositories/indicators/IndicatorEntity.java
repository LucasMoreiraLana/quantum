package com.example.quantum.repositories.indicators;


import com.example.quantum.domain.Indicator;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
@Document(collection = "indicators")
public class IndicatorEntity {

    UUID indicatorId;
    @NotNull
    String nameIndicator;
    @NotNull
    String measurementCriterion;
    @NotNull
    Sector sector;
    @NotNull
    UUID createdBy;
    @NotNull
    Indicator.UnitOfMeasurement unitOfMeasurement;
    @NotNull
    Indicator.FrequencyOfMeasurement frequencyOfMeasurement;
    @NotNull
    String objective;
    @NotNull
    boolean tendency;
    @NotNull
    LocalDate lastDate;
    @NotNull
    LocalDate nextMeasurement;
    @NotNull
    Indicator.Classification classification;

}

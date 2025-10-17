package com.example.quantum.repositories.indicators;


import com.example.quantum.domain.Indicator;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
@Document(collection = "indicators")
public class IndicatorEntity {

    @Id
    private UUID indicatorId = UUID.randomUUID();
    @NotNull
    @Size(max=30)
    private String nameIndicator;
    @NotNull
    private String measurementCriterion;
    @NotNull
    private Sector sector;
    @NotNull
    private UUID createdBy;
    @NotNull
    private Indicator.UnitOfMeasurement unitOfMeasurement;
    @NotNull
    private Indicator.FrequencyOfMeasurement frequencyOfMeasurement;
    @NotNull
    private String objective;
    @NotNull
    private boolean tendency;
    @NotNull
    private LocalDate lastDate;
    @NotNull
    private LocalDate nextMeasurement;
    @NotNull
    private Indicator.Classification classification;

}

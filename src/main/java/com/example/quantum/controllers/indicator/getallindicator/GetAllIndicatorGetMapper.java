package com.example.quantum.controllers.indicator.getallindicator;

import com.example.quantum.domain.Indicator;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllIndicatorGetMapper{

    public static GetAllIndicatorGetResponse toResponse(Indicator indicator){
        return new GetAllIndicatorGetResponse(
                indicator.indicatorId(),
                indicator.nameIndicator(),
                indicator.measurementCriterion(),
                indicator.sector(),
                indicator.createdBy(),
                indicator.unitOfMeasurement(),
                indicator.frequencyOfMeasurement(),
                indicator.objective(),
                indicator.tendency(),
                indicator.lastDate(),
                indicator.nextMeasurement(),
                indicator.classification()
        );
    }

    public static List<GetAllIndicatorGetResponse> toResponseList(List<Indicator> indicators){
        return indicators.stream()
                .map(GetAllIndicatorGetMapper::toResponse)
                .collect(Collectors.toList());
    }


}

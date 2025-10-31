package com.example.quantum.controllers.indicator.getindicator;

import com.example.quantum.domain.Indicator;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllIndicatorGetMapper{

    public static GetAllIndicatorGetResponse toIndicatorResponse(Indicator indicator){
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

    public static List<GetAllIndicatorGetResponse> toIndicatorResponseList(List<Indicator> indicators){
        return indicators.stream()
                .map(GetAllIndicatorGetMapper::toIndicatorResponse)
                .collect(Collectors.toList());
    }


}

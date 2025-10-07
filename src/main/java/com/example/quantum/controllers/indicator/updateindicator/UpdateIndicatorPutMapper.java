package com.example.quantum.controllers.indicator.updateindicator;

import com.example.quantum.domain.Indicator;
import com.example.quantum.services.indicator.UpdateIndicatorPutInput;

import java.util.UUID;

public class UpdateIndicatorPutMapper {

    public static UpdateIndicatorPutInput toInput(UUID indicatorId, UpdateIndicatorPutRequest request){
        return new UpdateIndicatorPutInput(
                indicatorId,
                request.nameIndicator(),
                request.measurementCriterion(),
                request.sector(),
                request.createdBy(),
                request.unitOfMeasurement(),
                request.frequencyOfMeasurement(),
                request.objective(),
                request.tendency(),
                request.lastDate(),
                request.nextMeasurement(),
                request.classification()
        );
    }


    public static UpdateIndicatorPutResponse toResponse(Indicator indicator){
        return new UpdateIndicatorPutResponse(
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
}

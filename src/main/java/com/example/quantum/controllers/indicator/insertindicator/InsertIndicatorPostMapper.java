package com.example.quantum.controllers.indicator.insertindicator;

import com.example.quantum.domain.Indicator;
import com.example.quantum.services.indicator.InsertIndicatorPostInput;

public class InsertIndicatorPostMapper {

    public static InsertIndicatorPostInput toInput(InsertIndicatorPostRequest request){
        return new InsertIndicatorPostInput(
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

    public static InsertIndicatorPostResponse toResponse(Indicator indicator) {
        return new InsertIndicatorPostResponse(
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

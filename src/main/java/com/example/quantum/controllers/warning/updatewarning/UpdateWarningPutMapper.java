package com.example.quantum.controllers.warning.updatewarning;


import com.example.quantum.services.warning.UpdateWarningPutInput;

import java.util.UUID;

public class UpdateWarningPutMapper {

    public static UpdateWarningPutInput toWarningInput(UUID warningId, UpdateWarningPutRequest request){
        return new UpdateWarningPutInput(
            warningId,
            request.warningTitle(),
            request.description(),
            request.processId(),
            request.sector(),
            request.probability(),
            request.impact(),
            request.level(),
            request.avaliation(),
            request.active()
        );
    }

}

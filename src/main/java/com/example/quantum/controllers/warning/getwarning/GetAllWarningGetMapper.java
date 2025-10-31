package com.example.quantum.controllers.warning.getwarning;


import com.example.quantum.domain.Warning;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllWarningGetMapper {


    public static GetAllWarningGetResponse toWarningResponse(Warning warning){
        return new GetAllWarningGetResponse(

                warning.warningTitle(),
                warning.createdBy(),
                warning.description(),
                warning.documentId(),
                warning.processId(),
                warning.sector(),
                warning.active()

        );
    }

    public static List<GetAllWarningGetResponse> toResponseWarningsList(List<Warning> warnings){
        return warnings.stream()
                .map(GetAllWarningGetMapper::toWarningResponse)
                .collect(Collectors.toList());
    }

}

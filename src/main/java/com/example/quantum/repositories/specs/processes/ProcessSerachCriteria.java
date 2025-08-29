package com.example.quantum.repositories.specs.processes;



import java.time.LocalDate;

import com.example.quantum.enums.CyclePDCA;
import com.example.quantum.enums.Sector;
import com.example.quantum.models.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProcessSerachCriteria {
    private String nameProcess;
    private LocalDate dateApproval;
    private LocalDate dateConclusion;

    private User userApproval;
    private Sector sector;
    private CyclePDCA cyclePDCA;

}   

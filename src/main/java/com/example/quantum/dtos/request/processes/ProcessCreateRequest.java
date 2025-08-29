package com.example.quantum.dtos.request.processes;

import java.time.LocalDate;

import com.example.quantum.enums.CyclePDCA;
import com.example.quantum.enums.Sector;

import jakarta.validation.constraints.NotBlank;

public record ProcessCreateRequest(
        @NotBlank(message = "O nome do processo não pode ser vazio!")
        String nameProcess,
        @NotBlank(message = "A data de aprovação do processo não pode ser vazia!")
        LocalDate dateApproval,
        LocalDate dateConclusion,
        @NotBlank(message = "O setor do processo precisa ser informado!")
        Sector sector,
        @NotBlank(message = "A fase do ciclo PDCA precisa ser informada!")
        CyclePDCA cyclePDCA
) {
    
}

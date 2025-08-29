package com.example.quantum.dtos.response.processes;
import java.time.LocalDate;
import java.util.UUID;
import com.example.quantum.enums.CyclePDCA;
import com.example.quantum.enums.Sector;


public record ProcessResponse(
        UUID idProcess,
        String nameProcess,
        LocalDate dateApproval,
        LocalDate dateConclusion,
        Sector sector,
        CyclePDCA cyclePDCA
) {
    
}

package com.example.quantum.domain;

import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


public record NonCompliance(

        UUID nonComplianceId,
        UUID createdBy,
        LocalDate dateOpening,
        UUID processId,
        Sector sector,
        Document.Origin origin,
        Priority priority,
        String customer,
        String description,
        boolean efficacy,
        LocalDate datePrevision

) {

}

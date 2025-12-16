package com.example.quantum.repositories.noncompliance;


import com.example.quantum.enums.DocumentOrigin;
import com.example.quantum.enums.Priority;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "non_compliance")
@NoArgsConstructor
@Getter
@Setter
public class NonComplianceEntity {

    @Id
    private UUID nonComplianceId;

    @NotNull
    private UUID createdBy; // O método getCreatedBy() é gerado aqui!

    @NotNull
    private UUID processId;

    @NotNull
    private LocalDate dateOpening; // CORRETO: dateOpening

    @NotNull
    private Sector sector;

    @NotNull
    private DocumentOrigin origin;

    @NotNull
    private Priority priority;

    @NotNull
    private String customer;

    @NotNull
    private String description;

    @NotNull
    private boolean efficacy;

    @NotNull
    private LocalDate datePrevision;

}
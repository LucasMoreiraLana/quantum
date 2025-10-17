package com.example.quantum.repositories.process;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.NIP;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "process")
@NoArgsConstructor
@Getter
@Setter
public class ProcessEntity {

    @Id
    private UUID processId;
    @NotNull
    private UUID createdBy;
    @NotNull
    @Size(max=30)
    private String nameProcess;
    @NotNull
    private LocalDate dateApproval;
    @NotNull
    private LocalDate dateConclusion;
    @NotNull
    private Sector sector;
    @NotNull
    private Process.Cycle cyclePDCA;

}

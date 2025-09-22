package com.example.quantum.repositories.process;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private UUID idProcess;

    @NotNull
    private UUID createdBy;

    private String nameProcess;

    private LocalDate dateApproval;

    private LocalDate dateConclusion;

    private Sector sector;

    private Process.Cycle cyclePDCA;
}

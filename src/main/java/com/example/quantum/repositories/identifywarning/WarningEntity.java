package com.example.quantum.repositories.identifywarning;

import com.example.quantum.domain.Process;
import com.example.quantum.enums.Sector;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "warnings")
@NoArgsConstructor
@Getter
@Setter
public class WarningEntity {

    @Id
    private UUID warningId = UUID.randomUUID();
    @NotBlank
    @Size(max=500)
    private String description;
    @NotNull
    private Process process;
    @NotNull
    private Sector sector;
    @NotBlank
    private int probability;
    @NotNull
    private int impact;
    @NotNull
    private int level;
    @NotNull
    private Avaliation avaliation;
    @NotBlank
    private boolean active;

    @NotNull
    private com.example.quantum.domain.Document document;
    @NotBlank
    private int newProbability;
    @NotBlank
    private int newImpact;
    @NotNull
    private int newLevel;
    @NotBlank
    private Avaliation newAvaliation;
    private UUID actionId;



    public enum Avaliation{
        RISCO_ALTO,
        RISCO_MEDIO
    }
}

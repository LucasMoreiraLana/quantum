package com.example.quantum.models;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.example.quantum.enums.Sector;
import com.example.quantum.enums.CyclePDCA;



@Entity
@Table(name = "process")
@Getter
@Setter
@NoArgsConstructor
public class Process {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProcess;
    private String nameProcess;
    private LocalDate dateApproval;
    private LocalDate dateConclusion;

    
    @ManyToOne
    @JoinColumn(name = "id_user_approval")
    private User userApproval;

    @NotNull(message = "O setor do processo precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Sector sector;

    @NotNull(message = "A fase do ciclo PDCA precisa ser informada!")
    @Enumerated(EnumType.STRING)
    private CyclePDCA cyclePDCA;

}

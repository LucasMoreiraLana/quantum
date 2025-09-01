package com.example.quantum.repositories.process;

import java.time.LocalDate;
import java.util.UUID;

import com.example.quantum.domain.Process;
import com.example.quantum.repositories.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.example.quantum.enums.Sector;


@Entity
@Table(name = "process")
@Getter
@Setter
@NoArgsConstructor
public class ProcessEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProcess;
    private String nameProcess;
    private LocalDate dateApproval;
    private LocalDate dateConclusion;

    
    @ManyToOne
    @JoinColumn(name = "id_user_approval")
    private UserEntity userEntityApproval;

    @NotNull(message = "O setor do processo precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Sector sector;

    @NotNull(message = "A fase do ciclo PDCA precisa ser informada!")
    @Enumerated(EnumType.STRING)
    private Process.Cycle cycle;

}

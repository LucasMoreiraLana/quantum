package com.example.quantum.models;

import org.hibernate.validator.constraints.UUID;

import com.example.quantum.enums.Sector;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@Table(name = "procediments")
@Getter
@Setter
@NoArgsConstructor
public class Procediment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idProcediment;
    private String nameProcediment;
    private String descriptionProcediment;

    @NotNull(message = "O setor do documento precisa ser informado!")
    @Enumerated(EnumType.STRING)
    private Sector sector;

    

}

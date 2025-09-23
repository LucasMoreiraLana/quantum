package com.example.quantum.repositories.action;

import com.example.quantum.domain.Action;
import com.example.quantum.enums.Sector;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;


@Document(collection = "actions")
public class ActionEntity {

    @Id
    private UUID idAction;

    private Action.TypeAction typeAction;

    private UUID nonCompliance;

    private String description;

    private String cost;

    private Sector sector;

    private UUID createdBy;

    private boolean status;

    private LocalDate datePrevision;







}

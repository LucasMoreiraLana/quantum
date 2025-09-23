package com.example.quantum.domain;

import com.example.quantum.enums.Sector;

import java.time.LocalDate;
import java.util.UUID;

public record Action(

        UUID idAction,
        TypeAction typeAction,
        UUID nonCompliance,
        String description,
        String cost,
        Sector sector,
        UUID createdBy,
        boolean status,
        LocalDate datePrevision
){

    public enum TypeAction{
        CORRETIVA,
        PREVENTIVA,
        IMEDIATA
    }




}

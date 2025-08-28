package com.example.quantum.repositories.specs.users;

import com.example.quantum.enums.Position;
import com.example.quantum.enums.Sector;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSearchCriteria {
    private String username;
    private String email;
    private Sector sector;
    private Position position;
    private Boolean onlyActive;
}

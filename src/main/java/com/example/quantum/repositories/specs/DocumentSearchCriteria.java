package com.example.quantum.repositories.specs;

import com.example.quantum.enums.DocumentType;
import com.example.quantum.enums.Origin;
import com.example.quantum.enums.Sector;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DocumentSearchCriteria {
    private String name;
    private Sector sector;
    private DocumentType type;
    private Origin origin;
    private Integer maxRetentionTime;
    
    @Builder.Default
    private boolean onlyActive = true;
}

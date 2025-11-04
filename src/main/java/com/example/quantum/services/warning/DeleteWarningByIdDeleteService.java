package com.example.quantum.services.warning;


import com.example.quantum.exceptions.WarningNotFoundException;
import com.example.quantum.repositories.warning.WarningEntity;
import com.example.quantum.repositories.warning.WarningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteWarningByIdDeleteService {

    @Autowired
    private WarningRepository warningRepository;

    public void deleteWarning(UUID warningId){
        WarningEntity warningEntity = warningRepository.findByWarningId(warningId)
                .orElseThrow(() -> new WarningNotFoundException("Risco não encontrado: " + warningId));
        warningEntity.setActive(false);
        warningRepository.save(warningEntity);
    }
}

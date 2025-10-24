package com.example.quantum.services.warning;

import com.example.quantum.domain.Warning;
import com.example.quantum.repositories.user.UserRepository;
import com.example.quantum.repositories.warning.WarningEntityMapper;
import com.example.quantum.repositories.warning.WarningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertWarningPostService {

    @Autowired
    private WarningRepository warningRepository;

    @Autowired
    private UserRepository userRepository;



    public Warning createWarning(InsertWarningPostInput input){

        if (!userRepository.existsById(input.createdBy())){
            throw new IllegalArgumentException("Usuário não encontrado!");
        }

        final var warning = input.toDomain();

        final var entity = WarningEntityMapper.toEntity(warning);

        final var saved = warningRepository.save(entity);

        return WarningEntityMapper.toWarning(saved);
    }
}

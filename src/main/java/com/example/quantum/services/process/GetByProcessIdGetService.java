package com.example.quantum.services.process;


import com.example.quantum.controllers.process.getprocess.GetProcessByIdGetMapper;
import com.example.quantum.domain.Process;
import com.example.quantum.repositories.process.ProcessRepository;
import com.example.quantum.repositories.user.UserRepository; // Importar o Repositório de Usuários
import com.example.quantum.repositories.user.UserEntity;     // Importar a Entidade de Usuário
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetByProcessIdGetService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private UserRepository userRepository; // NOVO: Para buscar o nome do criador

    // NOVO: Retorna o Optional do DTO de Saída
    public Optional<GetProcessServiceGetOutput> execute(GetByProcessIdGetInput input) {
        return processRepository.findById(input.processId())
                .map(GetProcessByIdGetMapper::toDomain)
                .map(process -> {
                    // 1. Buscar o ID do criador (Assumindo que Process domain tem createdBy())
                    final UUID createdBy = process.createdBy();

                    // 2. Buscar o nome do usuário (Username/Nome)
                    final Optional<String> createdByName = userRepository.findById(createdBy)
                            .map(UserEntity::getUsername); // Usando o getter do seu UserEntity

                    // 3. Empacotar e retornar
                    return new GetProcessServiceGetOutput(process, createdByName);
                });
    }

}
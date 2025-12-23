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

    public Optional<GetProcessServiceGetOutput> execute(GetByProcessIdGetInput input) {
        return processRepository.findById(input.processId())
                .map(GetProcessByIdGetMapper::toDomain)
                .map(process -> {
                    UUID createdBy = process.createdBy();
                    Optional<String> createdByName = Optional.empty();

                    // SÓ busca no banco se o ID do criador não for nulo
                    if (createdBy != null) {
                        createdByName = userRepository.findById(createdBy)
                                .map(UserEntity::getUsername);
                    }

                    return new GetProcessServiceGetOutput(process, createdByName);
                });
    }

}
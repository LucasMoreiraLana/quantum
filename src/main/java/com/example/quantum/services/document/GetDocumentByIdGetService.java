package com.example.quantum.services.document;


import com.example.quantum.controllers.document.getdocument.GetDocumentByIdGetMapper;
import com.example.quantum.services.document.GetDocumentServiceGetOutput;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.repositories.user.UserRepository; // NOVO IMPORT!
import com.example.quantum.repositories.user.UserEntity;     // NOVO IMPORT!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetDocumentByIdGetService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository; // NOVO: Para buscar o nome

    // O método agora retorna um Optional do nosso DTO de Saída
    public Optional<GetDocumentServiceGetOutput> execute(GetDocumentByIdGetInput input){

        return documentRepository.findById(input.documentId())
                .map(GetDocumentByIdGetMapper::toDomain)
                .map(document -> {
                    // 1. Buscar o ID do criador
                    final var createdBy = document.createdBy();

                    // 2. Buscar o nome do usuário, retornando um Optional<String>
                    final Optional<String> createdByName = userRepository.findById(createdBy)
                            .map(UserEntity::getUsername); // Assume que UserEntity tem getUsername()

                    // 3. Retornar o DTO de Saída, empacotando o Documento e o Nome
                    return new GetDocumentServiceGetOutput(document, createdByName);
                });
    }
}
package com.example.quantum.services.document;

import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.repositories.user.UserEntity;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;


@Service
public class InsertDocumentPostService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;


    // NOVO: O método agora retorna o DTO de Saída
    public InsertDocumentServicePostOutput createDocument(InsertDocumentPostInput input) {

        // 1. Input → Domain e Validação
        final var document = input.toDomain();

        if (documentRepository.existsByNameDocument(document.nameDocument())) {
            throw new IllegalArgumentException("Já existe um documento com esse nome!");
        }

        // 2. Persistência
        final var entity = DocumentEntityMapper.toEntity(document);
        final var savedEntity = documentRepository.save(entity);

        // 3. Entity → Domain
        final var savedDocument = DocumentEntityMapper.toDocument(savedEntity);

        // 4. Buscar o nome do criador
        final UUID createdBy = savedDocument.createdBy();

        System.out.println("DEBUG: Buscando nome para o ID do criador: " + createdBy);

        final String createdByName = userRepository.findById(createdBy)
                .map(UserEntity::getUsername) // Usando referência de método (mais limpo)
                .orElse("Usuário Desconhecido"); // Entra aqui se o findById falhar

        // 5. Retornar o DTO de Saída, empacotando o Documento e o Nome
        return new InsertDocumentServicePostOutput(savedDocument, createdByName);
    }
}
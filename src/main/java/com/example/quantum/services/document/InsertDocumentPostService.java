package com.example.quantum.services.document;


import com.example.quantum.domain.Document;
import com.example.quantum.repositories.document.DocumentEntityMapper;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InsertDocumentPostService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    public Document createDocument(InsertDocumentPostInput input) {

        if (!userRepository.existsById(input.createdBy())){
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
        // Input → Domain
        final var document = input.toDomain();

        // Domain → Entity
        final var entity = DocumentEntityMapper.toEntity(document);

        if (documentRepository.existsByNameDocument(document.nameDocument())) {
            throw new IllegalArgumentException("Já existe um documento com esse nome!");
        }

        // Salvar no banco
        final var savedEntity = documentRepository.save(entity);

        // Entity → Domain
        return DocumentEntityMapper.toDocument(savedEntity);

    }
}



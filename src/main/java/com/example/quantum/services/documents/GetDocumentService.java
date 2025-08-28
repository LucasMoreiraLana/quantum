package com.example.quantum.services.documents;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.quantum.dtos.response.documents.DocumentResponse;
import com.example.quantum.exceptions.DocumentNotFoundException;
import com.example.quantum.models.Document;
import com.example.quantum.repositories.specs.documents.DocumentSearchCriteria;
import com.example.quantum.repositories.specs.documents.DocumentSpecs;
import com.example.quantum.repositories.DocumentRepo;
import com.example.quantum.mappers.DocumentMapper;

@Service
@Transactional
@Validated
public class GetDocumentService{

    

    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private DocumentMapper documentMapper;

    public DocumentResponse findById(UUID id) {
        Document document = documentRepo.findByIdDocument(id)
            .orElseThrow(() -> new DocumentNotFoundException("Documento não encontrado: " + id));
        return documentMapper.toResponse(document);
    }

    /**
     * Busca documentos com critérios dinâmicos e paginação
     */
    public Page<DocumentResponse> searchDocuments(DocumentSearchCriteria criteria, Pageable pageable) {
        return documentRepo.findAll(DocumentSpecs.withDynamicQuery(criteria), pageable)
            .map(documentMapper::toResponse);
    }

    public List<DocumentResponse> findAllActive() {
        DocumentSearchCriteria criteria = DocumentSearchCriteria.builder()
            .onlyActive(true)
            .build();
        return documentRepo.findAll(DocumentSpecs.withDynamicQuery(criteria))
            .stream()
            .map(documentMapper::toResponse)
            .collect(Collectors.toList());

    }
}
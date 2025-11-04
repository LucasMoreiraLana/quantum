package com.example.quantum.services.warning;


import com.example.quantum.domain.Warning;
import com.example.quantum.repositories.document.DocumentRepository;
import com.example.quantum.repositories.process.ProcessRepository;
import com.example.quantum.repositories.warning.WarningEntityMapper;
import com.example.quantum.repositories.warning.WarningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UpdateWarningPutService {

    @Autowired
    private WarningRepository warningRepository;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public Warning updateWarning(UpdateWarningPutInput input){
        final var existingWarning = warningRepository.findByWarningId(input.warningId())
                .orElseThrow(() -> new RuntimeException("Risco não encontrado!"));

        final var existingProcess = processRepository.findById(input.processId())
                .orElseThrow(() -> new RuntimeException("Processo não encontrado."));

        final var existingDocument = documentRepository.findById(input.documentId())
                .orElseThrow(() -> new RuntimeException("Documento não encontrado."));

        final var updateWarning = new Warning(
                input.warningId(),
                input.warningTitle(),
                input.createdBy(),
                input.description(),
                existingProcess.getProcessId(),
                input.sector(),
                input.probability(),
                input.impact(),
                input.level(),
                input.avaliation(),
                input.active(),
                existingDocument.getDocumentId(),
                input.newProbability(),
                input.newImpact(),
                input.newLevel(),
                input.newAvaliation(),
                input.actions()
        );

        final var updatedEntity = WarningEntityMapper.toEntity(updateWarning);

        final var savedEntity = warningRepository.save(updatedEntity);

        return WarningEntityMapper.toWarning(savedEntity);
    }

}

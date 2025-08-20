package com.example.quantum.repositories.specs;

import com.example.quantum.models.Document;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class DocumentSpecs {
    
    public static Specification<Document> withDynamicQuery(DocumentSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Sempre incluir apenas documentos ativos por padrão
            if (criteria.isOnlyActive()) {
                predicates.add(criteriaBuilder.isTrue(root.get("active")));
            }

            // Adiciona critérios dinamicamente apenas se estiverem presentes
            if (criteria.getName() != null) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nameDocument")),
                    "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            if (criteria.getSector() != null) {
                predicates.add(criteriaBuilder.equal(root.get("sector"), criteria.getSector()));
            }

            if (criteria.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), criteria.getType()));
            }

            if (criteria.getOrigin() != null) {
                predicates.add(criteriaBuilder.equal(root.get("origin"), criteria.getOrigin()));
            }

            if (criteria.getMaxRetentionTime() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("tempoDeRetencao"), 
                    criteria.getMaxRetentionTime()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

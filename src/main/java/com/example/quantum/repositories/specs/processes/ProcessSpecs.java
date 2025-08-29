package com.example.quantum.repositories.specs.processes;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import com.example.quantum.models.Process;


public class ProcessSpecs {

    public static Specification<Process> withDynamicQuery(ProcessSerachCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getNameProcess() != null) {
                predicates.add(cb.like(cb.lower(root.get("nameProcess")),
                        "%" + criteria.getNameProcess().toLowerCase() + "%"));
            }

            if (criteria.getDateApproval() != null) {
                predicates.add(cb.equal(root.get("dateApproval"), criteria.getDateApproval()));
            }

            if (criteria.getDateConclusion() != null) {
                predicates.add(cb.equal(root.get("dateConclusion"), criteria.getDateConclusion()));
            }

            if (criteria.getSector() != null) {
                predicates.add(cb.equal(root.get("sector"), criteria.getSector()));
            }

            if (criteria.getCyclePDCA() != null) {
                predicates.add(cb.equal(root.get("cyclePDCA"), criteria.getCyclePDCA()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

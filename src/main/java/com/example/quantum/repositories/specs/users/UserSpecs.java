package com.example.quantum.repositories.specs.users;

import org.springframework.data.jpa.domain.Specification;

import com.example.quantum.models.User;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;



public class UserSpecs {
    
    public static Specification<User> withDynamicQuery(UserSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getUsername() != null) {
                predicates.add(cb.like(cb.lower(root.get("username")), 
                    "%" + criteria.getUsername().toLowerCase() + "%"));
            }

            if (criteria.getEmail() != null) {
                predicates.add(cb.like(cb.lower(root.get("email")), 
                    "%" + criteria.getEmail().toLowerCase() + "%"));
            }

            if (criteria.getSector() != null) {
                predicates.add(cb.equal(root.get("sector"), criteria.getSector()));
            }

            if (criteria.getPosition() != null) {
                predicates.add(cb.equal(root.get("position"), criteria.getPosition()));
            }

            if (criteria.getActive() != null && criteria.getActive()) {
                predicates.add(cb.isTrue(root.get("active")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

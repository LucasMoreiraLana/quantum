package com.example.quantum.repositories.document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface DocumentRepository extends MongoRepository<DocumentEntity, UUID>{
    
}
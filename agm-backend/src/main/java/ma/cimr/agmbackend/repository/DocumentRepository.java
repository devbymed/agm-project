package ma.cimr.agmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.cimr.agmbackend.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}

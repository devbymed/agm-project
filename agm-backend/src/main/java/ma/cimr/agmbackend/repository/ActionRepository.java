package ma.cimr.agmbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.cimr.agmbackend.model.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

}

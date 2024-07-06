package ma.cimr.agmbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.cimr.agmbackend.model.Assembly;

@Repository
public interface AssemblyRepository extends JpaRepository<Assembly, Long> {

	Optional<Assembly> findByClosed(boolean closed);
}

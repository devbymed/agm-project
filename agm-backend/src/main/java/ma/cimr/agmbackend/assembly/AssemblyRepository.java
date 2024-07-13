package ma.cimr.agmbackend.assembly;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssemblyRepository extends JpaRepository<Assembly, Long> {

	Optional<Assembly> findByClosed(boolean closed);

	boolean existsByClosed(boolean closed);
}

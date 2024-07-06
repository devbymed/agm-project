package ma.cimr.agmbackend.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.cimr.agmbackend.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findByName(String name);

	Set<Permission> findByNameIn(List<String> names);
}

package ma.cimr.agmbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.cimr.agmbackend.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Optional<Profile> findByName(String name);
}

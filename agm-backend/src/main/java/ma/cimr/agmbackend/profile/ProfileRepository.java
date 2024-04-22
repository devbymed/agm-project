package ma.cimr.agmbackend.profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Optional<Profile> findByName(String name);
}

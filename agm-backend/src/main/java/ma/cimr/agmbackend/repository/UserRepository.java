package ma.cimr.agmbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// import ma.cimr.agmbackend.model.Profile;
import ma.cimr.agmbackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	// User findByProfile(Profile profile);
}

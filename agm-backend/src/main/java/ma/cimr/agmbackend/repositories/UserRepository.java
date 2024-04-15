package ma.cimr.agmbackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.cimr.agmbackend.models.User;
import ma.cimr.agmbackend.models.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	User findByRole(Role role);
}

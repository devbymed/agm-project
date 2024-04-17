package ma.cimr.agmbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.model.Role;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	User findByRole(Role role);
}

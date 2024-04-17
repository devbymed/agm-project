package ma.cimr.agmbackend;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.repository.UserRepository;
import ma.cimr.agmbackend.model.Role;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class AgmBackendApplication implements CommandLineRunner {

	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(AgmBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if (adminAccount == null) {
			User admin = new User();
			admin.setFirstName("Imad");
			admin.setLastName("Lamrani");
			admin.setEmail("lamrani@cimr.com");
			admin.setRole(Role.ADMIN);
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			admin.setCreatedAt(LocalDateTime.now());
			userRepository.save(admin);
		}
	}
}
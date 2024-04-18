package ma.cimr.agmbackend;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.repository.ProfileRepository;
// import ma.cimr.agmbackend.repository.ProfileRepository;
import ma.cimr.agmbackend.repository.UserRepository;
import ma.cimr.agmbackend.model.Profile;
// import ma.cimr.agmbackend.model.Profile;
import ma.cimr.agmbackend.model.Role;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class AgmBackendApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AgmBackendApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner init(UserRepository userRepository,
	// ProfileRepository profileRepository,
	// PasswordEncoder passwordEncoder) {
	// return args -> {
	// Profile profile = new Profile();
	// profile.setName("ADMIN");
	// profileRepository.save(profile);

	// if (profileRepository.findById(profile.getId()).isPresent()) {
	// User admin = new User();
	// admin.setFirstName("Imad");
	// admin.setLastName("Lamrani");
	// admin.setEmail("lamrani@cimr.com");
	// admin.setProfile(profile);
	// admin.setPassword(passwordEncoder.encode("admin"));
	// userRepository.save(admin);
	// }
	// if (profileRepository.findByName("ADMIN") == null) {
	// User admin = new User();
	// admin.setFirstName("Imad");
	// admin.setLastName("Lamrani");
	// admin.setEmail("lamrani@cimr.com");
	// admin.setProfile(profile);
	// admin.setPassword(passwordEncoder.encode("admin"));
	// userRepository.save(admin);
	// }
	// if (userRepository.findByRole(Role.ADMIN) == null) {
	// userRepository.save(User.builder().firstName("Imad").lastName("Lamrani").email("lamrani@cimr.com")
	// .role(Role.ADMIN).password(passwordEncoder.encode("admin")).build());
	// }
	// };
	// }

	@Override
	public void run(String... args) throws Exception {
		Profile profile = new Profile();
		profile.setName("ADMIN");
		profile = profileRepository.save(profile);

		if (profileRepository.findById(profile.getId()).isPresent()) {
			User admin = new User();
			admin.setFirstName("Imad");
			admin.setLastName("Lamrani");
			admin.setEmail("lamrani@cimr.com");
			admin.setProfile(profile);
			admin.setPassword(passwordEncoder.encode("admin"));
			userRepository.save(admin);
		}
	}
}
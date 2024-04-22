package ma.cimr.agmbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.profile.Profile;
import ma.cimr.agmbackend.profile.ProfileRepository;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

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

	@Override
	public void run(String... args) throws Exception {
		if (profileRepository.findByName("ADMIN") != null) {
			Profile defaultProfile = Profile.builder().name("ADMIN").build();
			defaultProfile = profileRepository.save(defaultProfile);

			User adminAccount = User.builder()
					.firstName("Imad")
					.lastName("Lamrani")
					.email("lamrani@cimr.com")
					.profile(defaultProfile)
					.password(passwordEncoder.encode("admin"))
					.build();

			userRepository.save(adminAccount);
		}
	}
}
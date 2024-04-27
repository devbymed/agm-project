package ma.cimr.agmbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ma.cimr.agmbackend.profile.Profile;
import ma.cimr.agmbackend.profile.ProfileRepository;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

@Configuration
public class LoadDatabase {

	@Value("${USER_FIRST_NAME}")
	private String firstName;

	@Value("${USER_LAST_NAME}")
	private String lastName;

	@Value("${USER_EMAIL}")
	private String email;

	@Value("${USER_PASSWORD}")
	private String password;

	@Bean
	CommandLineRunner initDatabase(ProfileRepository profileRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Profile profile = profileRepository.findByName("Gestionnaire")
					.orElseGet(() -> profileRepository.save(Profile.builder().name("Gestionnaire").build()));

			userRepository.findByEmail(email)
					.orElseGet(() -> userRepository.save(User.builder()
							.firstName(firstName)
							.lastName(lastName)
							.email(email)
							.password(passwordEncoder.encode(password))
							.profile(profile)
							.build()));
		};
	}
}

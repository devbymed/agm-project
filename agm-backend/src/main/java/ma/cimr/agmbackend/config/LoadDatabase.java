package ma.cimr.agmbackend.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ma.cimr.agmbackend.feature.Feature;
import ma.cimr.agmbackend.feature.FeatureRepository;
import ma.cimr.agmbackend.profile.Profile;
import ma.cimr.agmbackend.profile.ProfileRepository;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

@Configuration
public class LoadDatabase {

	private static final List<String> FEATURE_NAMES = Arrays.asList("USER_MANAGEMENT", "AUTHORIZATIONS", "FDR_TRACKING");

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
			FeatureRepository featureRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Set<Feature> features = FEATURE_NAMES.stream()
					.map(name -> featureRepository.findByName(name)
							.orElseGet(() -> featureRepository.save(new Feature(name))))
					.collect(Collectors.toSet());
			Profile profile = profileRepository.findByName("Gestionnaire")
					.orElseGet(() -> {
						Profile newProfile = Profile.builder()
								.name("Gestionnaire").features(features).build();
						return profileRepository.save(newProfile);
					});

			userRepository.findByEmail(email)
					.orElseGet(() -> userRepository.save(User.builder()
							.firstName(firstName)
							.lastName(lastName)
							.email(email)
							.password(passwordEncoder.encode(password))
							.profile(profile)
							.isFirstLogin(false)
							.build()));
		};
	}
}

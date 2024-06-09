package ma.cimr.agmbackend.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ma.cimr.agmbackend.permission.Permission;
import ma.cimr.agmbackend.permission.PermissionRepository;
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
	CommandLineRunner initDatabase(ProfileRepository profileRepository,
			UserRepository userRepository,
			PermissionRepository permissionRepository,
			PasswordEncoder passwordEncoder) {
		List<String> permissionNames = Arrays.asList("Gestion utilisateurs",
				"Habilitations");
		return args -> {
			List<Permission> permissions = permissionNames.stream()
					.map(name -> permissionRepository.findByName(name)
							.orElse(permissionRepository.save(new Permission(name))))
					.collect(Collectors.toList());

			Profile profile = profileRepository.findByName("Gestionnaire")
					.orElseGet(() -> {
						Profile newProfile = Profile.builder()
								.name("Gestionnaire")
								.permissions(permissions)
								.build();
						return profileRepository.save(newProfile);
					});

			profileRepository.findByName("Agent de relance")
					.orElseGet(() -> {
						Profile newProfile = Profile.builder()
								.name("Agent de relance")
								.permissions(permissions)
								.build();
						return profileRepository.save(newProfile);
					});

			userRepository.findByEmail(email)
					.orElseGet(() -> userRepository.save(User.builder()
							.firstName(firstName)
							.lastName(lastName)
							.email(email)
							.username(email.split("@")[0])
							.password(passwordEncoder.encode(password))
							.profile(profile)
							.firstLogin(false)
							.build()));
		};
	}
}

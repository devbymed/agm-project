package ma.cimr.agmbackend.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cimr.agmbackend.permission.Permission;
import ma.cimr.agmbackend.permission.PermissionRepository;
import ma.cimr.agmbackend.profile.Profile;
import ma.cimr.agmbackend.profile.ProfileRepository;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

	@Value("${USER_FIRST_NAME}")
	private String firstName;

	@Value("${USER_LAST_NAME}")
	private String lastName;

	@Value("${USER_EMAIL}")
	private String email;

	@Value("${USER_PASSWORD}")
	private String password;

	private final ProfileRepository profileRepository;
	private final UserRepository userRepository;
	private final PermissionRepository permissionRepository;
	private final PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			log.info("Starting database initialization...");

			createPermissions();
			Profile gestionnaireProfile = createProfileIfNotFound("Gestionnaire", getGestionnairePermissions());
			Profile agentRelanceProfile = createProfileIfNotFound("Agent de relance", getAgentRelancePermissions());
			createUserIfNotFound(email, firstName, lastName, password, gestionnaireProfile);

			log.info("Database initialization completed.");
		};
	}

	private void createPermissions() {
		log.info("Creating permissions...");

		// Racine
		Permission preparationAssemblee = createPermissionIfNotFound("PREPARATION_ASSEMBLEE", "Préparation assemblée",
				"/preparation-assemblee");
		Permission administration = createPermissionIfNotFound("ADMINISTRATION", "Administration", "/administration");

		// Sous-permissions pour Préparation assemblée
		Permission nouvelleAssemblee = createPermissionIfNotFound("NEW_ASSEMBLY", "Nouvelle assemblée",
				"/preparation-assemblee/nouvelle-assemblee", preparationAssemblee);
		createPermissionIfNotFound("ONGOING_ASSEMBLY", "Assemblée en cours",
				"/preparation-assemblee/nouvelle-assemblee/assemblee-en-cours", nouvelleAssemblee);
		createPermissionIfNotFound("FOLLOW_FDR", "Suivi FDR", "/preparation-assemblee/nouvelle-assemblee/suivi-fdr",
				nouvelleAssemblee);
		Permission convocationAdherents = createPermissionIfNotFound("MEMBER_INVITATION", "Convocation adhérents",
				"/preparation-assemblee/convocation-adherents", preparationAssemblee);
		createPermissionIfNotFound("MEMBER_LIST", "Liste des adhérents",
				"/preparation-assemblee/convocation-adherents/liste-adherents", convocationAdherents);
		Permission convocation = createPermissionIfNotFound("INVITATION", "Convocation",
				"/preparation-assemblee/convocation-adherents/convocation", convocationAdherents);
		createPermissionIfNotFound("MEMBER_ASSIGNMENT", "Affectation des adhérents",
				"/preparation-assemblee/convocation-adherents/convocation/affectation-adherents", convocation);
		createPermissionIfNotFound("INVITATION_GENERATION", "Génération des convocations",
				"/preparation-assemblee/convocation-adherents/convocation/generation-convocations", convocation);
		createPermissionIfNotFound("ASSIGNMENT_CONSULTATION", "Consultation des affectations",
				"/preparation-assemblee/convocation-adherents/convocation/consultation-affectations", convocation);
		createPermissionIfNotFound("INVITATION_STATUS_CONSULTATION", "Consultation état des convocations",
				"/preparation-assemblee/convocation-adherents/convocation/consultation-etat-convocations", convocation);
		Permission regroupements = createPermissionIfNotFound("GROUPINGS", "Regroupements",
				"/preparation-assemblee/regroupements", convocationAdherents);
		createPermissionIfNotFound("SCANNING", "Numérisation", "/preparation-assemblee/regroupements/numerisation",
				regroupements);
		createPermissionIfNotFound("QUALIFICATION", "Qualification", "/preparation-assemblee/regroupements/qualification",
				regroupements);
		createPermissionIfNotFound("ELIGIBLE_MEMBER_LIST", "Liste des adhérents éligibles par regroupement",
				"/preparation-assemblee/regroupements/liste-adherents-eligibles", regroupements);
		createPermissionIfNotFound("GROUPINGS_CONSULTATION", "Consultations des regroupements",
				"/preparation-assemblee/regroupements/consultation-regroupements", regroupements);
		createPermissionIfNotFound("MAIL_RETURN_MANAGEMENT", "Gestion des retours courriers",
				"/preparation-assemblee/convocation-adherents/gestion-retours-courriers", convocationAdherents);

		// Sous-permissions pour Administration
		createPermissionIfNotFound("AUTHORIZATIONS", "Habilitations", "/administration/habilitations", administration);
		createPermissionIfNotFound("USER_MANAGEMENT", "Gestion utilisateurs", "/administration/gestion-utilisateurs",
				administration);
		createPermissionIfNotFound("CONFIGURATION", "Paramétrage", "/administration/parametrage", administration);
	}

	private Profile createProfileIfNotFound(String name, Set<Permission> permissions) {
		return profileRepository.findByName(name)
				.orElseGet(() -> {
					Profile newProfile = Profile.builder()
							.name(name)
							.permissions(permissions)
							.build();
					return profileRepository.save(newProfile);
				});
	}

	private void createUserIfNotFound(String email, String firstName, String lastName, String password, Profile profile) {
		userRepository.findByEmail(email)
				.orElseGet(() -> {
					User user = User.builder()
							.firstName(firstName)
							.lastName(lastName)
							.email(email)
							.username(email.split("@")[0])
							.password(passwordEncoder.encode(password))
							.profile(profile)
							.firstLogin(false)
							.build();
					return userRepository.save(user);
				});
	}

	private Permission createPermissionIfNotFound(String name, String label, String path) {
		return permissionRepository.findByName(name)
				.orElseGet(() -> permissionRepository.save(
						Permission.builder()
								.name(name)
								.label(label)
								.path(path)
								.build()));
	}

	private Permission createPermissionIfNotFound(String name, String label, String path, Permission parent) {
		return permissionRepository.findByName(name)
				.orElseGet(() -> permissionRepository.save(
						Permission.builder()
								.name(name)
								.label(label)
								.path(path)
								.parent(parent)
								.build()));
	}

	private Set<Permission> getGestionnairePermissions() {
		return new HashSet<>(permissionRepository.findAll());
	}

	private Set<Permission> getAgentRelancePermissions() {
		List<String> permissionNames = Arrays.asList("MEMBER_LIST", "GROUPINGS_CONSULTATION", "MAIL_RETURN_MANAGEMENT");
		return new HashSet<>(permissionRepository.findByNameIn(permissionNames));
	}
}

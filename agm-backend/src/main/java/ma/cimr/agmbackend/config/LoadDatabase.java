package ma.cimr.agmbackend.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
		return args -> {
			// Création des permissions racines
			Permission preparationAssemblee = createPermissionIfNotFound(permissionRepository, "PREPARATION_ASSEMBLEE",
					"Préparation assemblée");
			Permission administration = createPermissionIfNotFound(permissionRepository, "ADMINISTRATION", "Administration");

			// Création des sous-permissions pour Préparation assemblée
			Permission nouvelleAssemblee = createPermissionIfNotFound(permissionRepository, "NEW_ASSEMBLY",
					"Nouvelle assemblée", preparationAssemblee);
			Permission assembleeEnCours = createPermissionIfNotFound(permissionRepository, "ONGOING_ASSEMBLY",
					"Assemblée en cours", nouvelleAssemblee);
			Permission suiviFDR = createPermissionIfNotFound(permissionRepository, "FOLLOW_FDR", "Suivi FDR",
					nouvelleAssemblee);
			Permission convocationAdherents = createPermissionIfNotFound(permissionRepository, "MEMBER_INVITATION",
					"Convocation adhérents", preparationAssemblee);
			Permission listeAdherents = createPermissionIfNotFound(permissionRepository, "MEMBER_LIST", "Liste des adhérents",
					convocationAdherents);
			Permission convocation = createPermissionIfNotFound(permissionRepository, "INVITATION", "Convocation",
					convocationAdherents);
			Permission affectationDesAdherents = createPermissionIfNotFound(permissionRepository, "MEMBER_ASSIGNMENT",
					"Affectation des adhérents", convocation);
			Permission generationDesConvocations = createPermissionIfNotFound(permissionRepository, "INVITATION_GENERATION",
					"Génération des convocations", convocation);
			Permission consultationDesAffectations = createPermissionIfNotFound(permissionRepository,
					"ASSIGNMENT_CONSULTATION", "Consultation des affectations", convocation);
			Permission consultationEtatDesConvocations = createPermissionIfNotFound(permissionRepository,
					"INVITATION_STATUS_CONSULTATION", "Consultation état des convocations", convocation);
			Permission regroupements = createPermissionIfNotFound(permissionRepository, "GROUPINGS", "Regroupements",
					convocationAdherents);
			Permission numerisation = createPermissionIfNotFound(permissionRepository, "SCANNING", "Numérisation",
					regroupements);
			Permission qualification = createPermissionIfNotFound(permissionRepository, "QUALIFICATION", "Qualification",
					regroupements);
			Permission listeDesAdherentsEligibles = createPermissionIfNotFound(permissionRepository, "ELIGIBLE_MEMBER_LIST",
					"Liste des adhérents éligibles par regroupement", regroupements);
			Permission consultationsDesRegroupements = createPermissionIfNotFound(permissionRepository,
					"GROUPINGS_CONSULTATION", "Consultations des regroupements", regroupements);
			Permission gestionDesRetoursCourriers = createPermissionIfNotFound(permissionRepository, "MAIL_RETURN_MANAGEMENT",
					"Gestion des retours courriers", convocationAdherents);

			// Création des sous-permissions pour Administration
			Permission habilitations = createPermissionIfNotFound(permissionRepository, "AUTHORIZATIONS",
					"Habilitations", administration);
			Permission gestionUtilisateurs = createPermissionIfNotFound(permissionRepository, "USER_MANAGEMENT",
					"Gestion utilisateurs", administration);
			Permission parametrage = createPermissionIfNotFound(permissionRepository, "CONFIGURATION", "Paramétrage",
					administration);

			// Ajout des permissions à la liste du profil Gestionnaire
			List<Permission> permissionsGestionnaire = Arrays.asList(preparationAssemblee, administration,
					nouvelleAssemblee, assembleeEnCours, suiviFDR, convocationAdherents, listeAdherents, convocation,
					affectationDesAdherents, generationDesConvocations, consultationDesAffectations,
					consultationEtatDesConvocations, regroupements, numerisation, qualification,
					listeDesAdherentsEligibles, consultationsDesRegroupements, gestionDesRetoursCourriers,
					habilitations, gestionUtilisateurs, parametrage);

			// Ajout des permissions à la liste du profil Agent de relance
			List<Permission> permissionsAgentRelance = Arrays.asList(listeAdherents, consultationsDesRegroupements,
					gestionDesRetoursCourriers);

			// Création des profils avec leurs permissions
			Profile gestionnaireProfile = profileRepository.findByName("Gestionnaire")
					.orElseGet(() -> {
						Profile newProfile = Profile.builder()
								.name("Gestionnaire")
								.permissions(new HashSet<>(permissionsGestionnaire))
								.build();
						return profileRepository.save(newProfile);
					});

			Profile agentRelanceProfile = profileRepository.findByName("Agent de relance")
					.orElseGet(() -> {
						Profile newProfile = Profile.builder()
								.name("Agent de relance")
								.permissions(new HashSet<>(permissionsAgentRelance))
								.build();
						return profileRepository.save(newProfile);
					});

			// Création de l'utilisateur gestionnaire
			userRepository.findByEmail(email)
					.orElseGet(() -> userRepository.save(User.builder()
							.firstName(firstName)
							.lastName(lastName)
							.email(email)
							.username(email.split("@")[0])
							.password(passwordEncoder.encode(password))
							.profile(gestionnaireProfile)
							.firstLogin(false)
							.build()));
		};
	}

	private Permission createPermissionIfNotFound(PermissionRepository permissionRepository, String name, String label) {
		return permissionRepository.findByName(name)
				.orElseGet(() -> permissionRepository.save(
						Permission.builder().name(name).label(label).build()));
	}

	private Permission createPermissionIfNotFound(PermissionRepository permissionRepository, String name, String label,
			Permission parent) {
		return permissionRepository.findByName(name)
				.orElseGet(() -> permissionRepository.save(
						Permission.builder().name(name).label(label).parent(parent).build()));
	}
}

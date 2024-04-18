package ma.cimr.agmbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
// import ma.cimr.agmbackend.model.Profile;
import ma.cimr.agmbackend.model.Role;

@Getter
@Setter
@Builder
public class UserCreateRequest {

	@NotBlank(message = "Le prénom est obligatoire")
	private String firstName;

	@NotBlank(message = "Le nom est obligatoire")
	private String lastName;

	@Email(message = "L'email doit être valide")
	@NotBlank(message = "L'email est obligatoire")
	private String email;

	// @NotNull(message = "L'affectation du profil est obligatoire")
	// private Profile profile;
}

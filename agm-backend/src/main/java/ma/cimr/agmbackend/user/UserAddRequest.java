package ma.cimr.agmbackend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest {
	@NotBlank(message = "Le prénom est obligatoire")
	private String firstName;

	@NotBlank(message = "Le nom est obligatoire")
	private String lastName;

	@Email(message = "L'email doit être valide")
	@NotBlank(message = "L'email est obligatoire")
	private String email;

	@NotNull(message = "L'affectation du profil est obligatoire")
	private Long profileId;
}

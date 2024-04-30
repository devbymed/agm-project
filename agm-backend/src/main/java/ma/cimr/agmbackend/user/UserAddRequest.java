package ma.cimr.agmbackend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
	@Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
	private String firstName;

	@NotBlank(message = "Le nom est obligatoire")
	@Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
	private String lastName;

	@Email(message = "L'email doit être valide")
	@NotBlank(message = "L'email est obligatoire")
	@Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
	private String email;

	@Positive(message = "L'identifiant du profil ne peut pas être négatif")
	@NotNull(message = "L'affectation du profil est obligatoire")
	private Long profileId;
}

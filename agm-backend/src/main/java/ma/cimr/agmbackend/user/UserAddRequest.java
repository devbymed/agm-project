package ma.cimr.agmbackend.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserAddRequest(
		@NotBlank(message = "Le prénom est obligatoire") @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères") String firstName,

		@NotBlank(message = "Le nom est obligatoire") @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères") String lastName,

		@Email(message = "L'email doit être valide") @NotBlank(message = "L'email est obligatoire") @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères") String email,

		@Positive(message = "L'identifiant du profil ne peut pas être négatif") @NotNull(message = "L'affectation du profil est obligatoire") Long profileId) {
}

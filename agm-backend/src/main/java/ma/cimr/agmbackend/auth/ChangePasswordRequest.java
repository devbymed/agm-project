package ma.cimr.agmbackend.auth;

import jakarta.validation.constraints.NotBlank;
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
public class ChangePasswordRequest {

	@NotBlank(message = "Le mot de passe actuel est obligatoire")
	private String currentPassword;

	@NotBlank(message = "Le nouveau mot de passe est obligatoire")
	@Size(min = 8, max = 20, message = "Le mot de passe doit contenir entre 8 et 20 caractères")
	private String newPassword;

	@NotBlank(message = "La confirmation du mot de passe est obligatoire")
	private String confirmPassword;
}

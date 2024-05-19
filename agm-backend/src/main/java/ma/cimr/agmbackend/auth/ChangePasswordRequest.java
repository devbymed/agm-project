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

	@NotBlank(message = "L'ancien mot de passe est obligatoire")
	private String oldPassword;

	@NotBlank(message = "Le nouveau mot de passe est obligatoire")
	@Size(min = 8, max = 20, message = "Le mot de passe doit contenir entre 8 et 20 caractères")
	private String newPassword;
}

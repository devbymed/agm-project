package ma.cimr.agmbackend.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthRequest {
	@NotBlank(message = "Veillez saisir votre identifiant")
	private String email;

	@NotBlank(message = "Veillez saisir votre mot de passe")
	private String password;
}

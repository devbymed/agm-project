package ma.cimr.agmbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class AuthRequest {
	@NotBlank(message = "Veillez saisir votre identifiant")
	private String username;

	@NotBlank(message = "Veillez saisir votre mot de passe")
	private String password;
}
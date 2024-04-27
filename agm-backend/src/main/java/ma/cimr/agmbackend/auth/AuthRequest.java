package ma.cimr.agmbackend.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
		@NotBlank(message = "Veillez saisir votre identifiant") String email,
		@NotBlank(message = "Veillez saisir votre mot de passe") String password) {
}
package ma.cimr.agmbackend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionCodes {
	BAD_CREDENTIALS(1000, "Identifiant et/ou mot de passe incorrect(s)", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN(1001, "Token invalide", HttpStatus.UNAUTHORIZED),
	USER_NOT_FOUND(2000, "Utilisateur non trouvé", HttpStatus.NOT_FOUND),
	PROFILE_NOT_FOUND(3000, "Profil non trouvé", HttpStatus.NOT_FOUND);

	private final int code;
	private final String message;
	private final HttpStatus status;
}
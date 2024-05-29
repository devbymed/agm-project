package ma.cimr.agmbackend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionCodes {

	BAD_CREDENTIALS("Identifiant et/ou mot de passe incorrect(s)", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN("Token invalide", HttpStatus.UNAUTHORIZED),
	INCORRECT_CURRENT_PASSWORD("Le mot de passe actuel est incorrect", HttpStatus.BAD_REQUEST),
	MISMATCHED_PASSWORDS("Les mots de passe ne correspondent pas", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND("Utilisateur non trouvé", HttpStatus.NOT_FOUND),
	PROFILE_NOT_FOUND("Profil non trouvé", HttpStatus.NOT_FOUND),
	PERMISSION_NOT_FOUND("Permission non trouvée", HttpStatus.NOT_FOUND);

	private final String message;
	private final HttpStatus status;
}
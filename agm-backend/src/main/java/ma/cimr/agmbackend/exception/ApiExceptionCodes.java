package ma.cimr.agmbackend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiExceptionCodes {

	BAD_CREDENTIALS("Identifiant ou mot de passe incorrect", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN("Token invalide", HttpStatus.UNAUTHORIZED),
	INCORRECT_CURRENT_PASSWORD("Le mot de passe actuel est incorrect", HttpStatus.BAD_REQUEST),
	SAME_AS_CURRENT_PASSWORD("Le nouveau mot de passe doit différer de l'actuel", HttpStatus.BAD_REQUEST),
	MISMATCHED_PASSWORDS("Les mots de passe ne correspondent pas", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND("Utilisateur non trouvé", HttpStatus.NOT_FOUND),
	EMAIL_ALREADY_EXISTS("Cet email existe déjà", HttpStatus.BAD_REQUEST),
	PROFILE_NOT_FOUND("Profil non trouvé", HttpStatus.NOT_FOUND),
	PERMISSION_NOT_FOUND("Permission non trouvée", HttpStatus.NOT_FOUND);

	private final String message;
	private final HttpStatus status;
}
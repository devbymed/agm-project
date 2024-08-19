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
	PROFILE_ALREADY_EXISTS("Ce profil existe déjà", HttpStatus.BAD_REQUEST),
	PERMISSION_NOT_FOUND("Permission non trouvée", HttpStatus.NOT_FOUND),
	REASON_NOT_FOUND("Motif non trouvé", HttpStatus.NOT_FOUND),
	ASSEMBLY_NOT_FOUND("Assemblée non trouvée", HttpStatus.NOT_FOUND),
	CURRENT_ASSEMBLY_NOT_FOUND("Aucune assemblée en cours trouvée", HttpStatus.NOT_FOUND),
	ASSEMBLY_ALREADY_CLOSED("L'assemblée est déjà clôturée", HttpStatus.BAD_REQUEST),
	ACTION_NOT_FOUND("Action non trouvée", HttpStatus.NOT_FOUND),
	DOCUMENT_UPLOAD_FAILED("Échec du téléchargement du document", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_DOCUMENT_FORMAT("Format de document invalide", HttpStatus.BAD_REQUEST),
	MEMBER_NOT_FOUND("Membre non trouvé", HttpStatus.NOT_FOUND);

	private final String message;
	private final HttpStatus status;
}
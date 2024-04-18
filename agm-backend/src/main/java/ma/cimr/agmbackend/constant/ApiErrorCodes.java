package ma.cimr.agmbackend.constant;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiErrorCodes {
	BAD_CREDENTIALS(1001, "Le nom d'utilisateur ou le mot de passe est incorrect", HttpStatus.UNAUTHORIZED);

	private final int errorCode;
	private final String errorMessage;
	private final HttpStatus responseStatus;
}
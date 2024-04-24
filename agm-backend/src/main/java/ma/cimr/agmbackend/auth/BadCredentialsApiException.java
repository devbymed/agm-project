package ma.cimr.agmbackend.auth;

import lombok.Getter;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

@Getter
public class BadCredentialsApiException extends ApiException {

	public BadCredentialsApiException() {
		super(ApiExceptionCodes.BAD_CREDENTIALS);
	}
}

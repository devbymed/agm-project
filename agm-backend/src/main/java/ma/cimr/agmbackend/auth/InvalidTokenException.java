package ma.cimr.agmbackend.auth;

import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

public class InvalidTokenException extends ApiException {
	public InvalidTokenException() {
		super(ApiExceptionCodes.INVALID_TOKEN);
	}
}

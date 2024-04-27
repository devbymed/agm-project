package ma.cimr.agmbackend.user;

import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

public class UserNotFoundException extends ApiException {
	public UserNotFoundException() {
		super(ApiExceptionCodes.USER_NOT_FOUND);
	}
}

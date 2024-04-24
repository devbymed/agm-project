package ma.cimr.agmbackend.user;

import lombok.Getter;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

@Getter
public class UserNotFoundException extends ApiException {

	public UserNotFoundException() {
		super(ApiExceptionCodes.USER_NOT_FOUND);
	}
}

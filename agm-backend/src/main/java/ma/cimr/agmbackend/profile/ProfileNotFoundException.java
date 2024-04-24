package ma.cimr.agmbackend.profile;

import lombok.Getter;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

@Getter
public class ProfileNotFoundException extends ApiException {

	public ProfileNotFoundException() {
		super(ApiExceptionCodes.PROFILE_NOT_FOUND);
	}
}

package ma.cimr.agmbackend.auth;

import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;

public class BadCredentialsApiException extends ApiException {
    public BadCredentialsApiException() {
        super(ApiExceptionCodes.BAD_CREDENTIALS);
    }
}

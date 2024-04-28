package ma.cimr.agmbackend.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private final HttpStatus status;

	public ApiException(ApiExceptionCodes apiExceptionCodes) {
		super(apiExceptionCodes.getMessage());
		this.status = apiExceptionCodes.getStatus();
	}

	public ApiException(ApiExceptionCodes apiExceptionCodes, Throwable cause) {
		super(apiExceptionCodes.getMessage(), cause);
		this.status = apiExceptionCodes.getStatus();
	}
}

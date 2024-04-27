package ma.cimr.agmbackend.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(ex.getStatus())
				.message(ex.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiExceptionResponse> handleException(Exception ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.message("Une erreur inattendue s'est produite")
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST)
				.validationErrors(errors)
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED)
				.message(ApiExceptionCodes.BAD_CREDENTIALS.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}
}

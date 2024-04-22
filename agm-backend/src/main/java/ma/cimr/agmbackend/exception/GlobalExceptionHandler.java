package ma.cimr.agmbackend.exception;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ma.cimr.agmbackend.user.UserNotFoundException;

@RestControllerAdvice
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
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ApiExceptionResponse> handleException(Exception ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.message("Une erreur inattendue s'est produite")
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Set<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toSet());
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST)
				.message("Les données fournies ne sont pas valides")
				.validationErrors(errors)
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED)
				.message(ApiExceptionCodes.BAD_CREDENTIALS.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(ex.getStatus())
				.message(ex.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}
}

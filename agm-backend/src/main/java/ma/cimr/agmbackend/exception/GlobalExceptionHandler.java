package ma.cimr.agmbackend.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException ex) {
		LOGGER.error("An API exception occurred with message: {}", ex.getMessage());
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
		LOGGER.error("An API exception occurred with message: {}", ex.getMessage());
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
		LOGGER.error("An API exception occurred with message: {}", ex.getMessage());
		Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
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
		LOGGER.error("An API exception occurred with message: {}", ex.getMessage());
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.UNAUTHORIZED)
				.message(ApiExceptionCodes.BAD_CREDENTIALS.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getStatus());
	}
}

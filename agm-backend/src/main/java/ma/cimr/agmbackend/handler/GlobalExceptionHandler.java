package ma.cimr.agmbackend.handler;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.validator.cfg.context.ConstraintDefinitionContext.ConstraintValidatorDefinitionContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import ma.cimr.agmbackend.constant.ApiErrorCodes;
import ma.cimr.agmbackend.dto.response.ApiExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ApiExceptionResponse> handleException(Exception ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.errorOccurredAt(LocalDateTime.now())
				.responseHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.specificErrorMessage("Une erreur inattendue s'est produite")
				// .generalErrorDescription(ex.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getResponseHttpStatus());
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.errorOccurredAt(LocalDateTime.now())
				.responseHttpStatus(HttpStatus.UNAUTHORIZED)
				.specificErrorMessage(ApiErrorCodes.BAD_CREDENTIALS.getErrorMessage())
				// .generalErrorDescription(ex.getMessage())
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getResponseHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiExceptionResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		Set<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage)
				.collect(Collectors.toSet());
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.errorOccurredAt(LocalDateTime.now())
				.responseHttpStatus(HttpStatus.BAD_REQUEST)
				.specificErrorMessage("Les données fournies ne sont pas valides")
				// .generalErrorDescription(ex.getMessage())
				.inputValidationErrors(validationErrors)
				.build();
		return new ResponseEntity<ApiExceptionResponse>(response, response.getResponseHttpStatus());
	}
}

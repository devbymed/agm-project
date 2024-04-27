package ma.cimr.agmbackend.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(NON_EMPTY)
public class ApiExceptionResponse {

	private final LocalDateTime timestamp;
	private final HttpStatus status;

	@Nullable
	private final String message;

	@Nullable
	private final Map<String, String> validationErrors;

	@Nullable
	private final Map<String, Object> additionalDetails;
}

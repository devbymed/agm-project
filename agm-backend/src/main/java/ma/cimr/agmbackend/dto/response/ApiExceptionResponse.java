package ma.cimr.agmbackend.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_EMPTY)
public class ApiExceptionResponse {
	private LocalDateTime errorOccurredAt;
	private HttpStatus responseHttpStatus;
	private String specificErrorMessage;
	// private String generalErrorDescription;
	private Set<String> inputValidationErrors;
	private Map<String, Object> additionalErrorDetails;
}

package ma.cimr.agmbackend.util;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(NON_NULL)
public class ApiResponse {
	private HttpStatus status;
	private String message;
	private Object data;

	public ApiResponse(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public ApiResponse(HttpStatus status, Object data) {
		this.status = status;
		this.data = data;
	}

	public ApiResponse(HttpStatus status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
}

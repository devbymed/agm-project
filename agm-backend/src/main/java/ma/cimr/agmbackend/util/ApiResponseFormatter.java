package ma.cimr.agmbackend.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseFormatter {
	public static ResponseEntity<ApiResponse> generateResponse(HttpStatus status, String message) {
		ApiResponse response = ApiResponse.builder()
				.status(status)
				.message(message)
				.build();
		return new ResponseEntity<ApiResponse>(response, status);
	}

	public static ResponseEntity<ApiResponse> generateResponse(HttpStatus status, Object data) {
		ApiResponse response = ApiResponse.builder()
				.status(status)
				.data(data)
				.build();
		return new ResponseEntity<ApiResponse>(response, status);
	}

	public static ResponseEntity<ApiResponse> generateResponse(HttpStatus status, String message,
			Object data) {
		ApiResponse response = ApiResponse.builder()
				.status(status)
				.message(message)
				.data(data)
				.build();
		return new ResponseEntity<ApiResponse>(response, status);
	}
}
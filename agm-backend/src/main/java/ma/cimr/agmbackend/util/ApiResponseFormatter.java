package ma.cimr.agmbackend.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseFormatter {

	public static ResponseEntity<ApiResponse> generateResponse(HttpStatus status, String message) {
		ApiResponse response = new ApiResponse(status, message);
		return new ResponseEntity<ApiResponse>(response, status);
	}

	public static ResponseEntity<ApiResponse> generateResponse(HttpStatus status, Object data) {
		ApiResponse response = new ApiResponse(status, data);
		return new ResponseEntity<ApiResponse>(response, status);
	}

	public static ResponseEntity<ApiResponse> generateResponse(HttpStatus status, String message,
			Object data) {
		ApiResponse response = new ApiResponse(status, message, data);
		return new ResponseEntity<ApiResponse>(response, status);
	}
}
package ma.cimr.agmbackend.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseFormatter {
	public static ResponseEntity<Map<String, Object>> generateResponse(HttpStatus status, String message,
			Object data) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", status);
		response.put("message", message);
		response.put("data", data);
		return new ResponseEntity<Map<String, Object>>(response, status);
	}
}
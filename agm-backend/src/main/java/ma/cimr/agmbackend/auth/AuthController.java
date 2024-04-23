package ma.cimr.agmbackend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.util.ApiResponse;
import ma.cimr.agmbackend.util.ApiResponseFormatter;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, authService.authenticateUser(authRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse> renewAccessToken(
			@RequestBody TokenRefreshRequest tokenRefreshRequest) {
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, authService.renewAccessToken(tokenRefreshRequest));
	}
}

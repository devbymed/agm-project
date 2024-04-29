package ma.cimr.agmbackend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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
		AuthResponse response = authService.authenticateUser(authRequest);
		if (response.isMustChangePassword()) {
			return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Vous devez changer votre mot de passe", response);
		}
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	}

	// @PostMapping("/refresh-token")
	// public ResponseEntity<ApiResponse> renewAccessToken(
	// 		@RequestBody TokenRefreshRequest tokenRefreshRequest) {
	// 	AuthResponse response = authService.renewAccessToken(tokenRefreshRequest);
	// 	return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	// }

	@PatchMapping("/change-password")
	public ResponseEntity<ApiResponse> changePassword(
			@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		authService.changePassword(changePasswordRequest);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Le mot de passe a été modifié avec succès");
	}
}

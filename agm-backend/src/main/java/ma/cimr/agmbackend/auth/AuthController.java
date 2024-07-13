package ma.cimr.agmbackend.auth;

import java.security.Principal;

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
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, response);
	}

	@PatchMapping("/change-password")
	public ResponseEntity<ApiResponse> changePassword(
			@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal authenticatedUser) {
		authService.changePassword(changePasswordRequest, authenticatedUser);
		return ApiResponseFormatter.generateResponse(HttpStatus.OK, "Le mot de passe a été modifié avec succès");
	}
}

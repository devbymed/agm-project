package ma.cimr.agmbackend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.login(authRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> renewAccessToken(
			@RequestBody TokenRefreshRequest tokenRefreshRequest) {
		return ResponseEntity.ok(authService.renewAccessToken(tokenRefreshRequest));
	}
}

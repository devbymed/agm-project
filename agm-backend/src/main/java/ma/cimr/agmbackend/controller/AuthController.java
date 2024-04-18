package ma.cimr.agmbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.TokenRefreshRequest;
import ma.cimr.agmbackend.dto.request.UserLoginRequest;
import ma.cimr.agmbackend.dto.response.UserLoginResponse;
import ma.cimr.agmbackend.service.AuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
		return ResponseEntity.ok(authService.login(userLoginRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<UserLoginResponse> renewAccessTokenUsingRefreshToken(
			@RequestBody TokenRefreshRequest tokenRefreshRequest) {
		return ResponseEntity.ok(authService.renewAccessTokenUsingRefreshToken(tokenRefreshRequest));
	}
}

package ma.cimr.agmbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.TokenRefreshRequest;
import ma.cimr.agmbackend.dto.request.UserLoginRequest;
import ma.cimr.agmbackend.dto.response.UserLoginResponse;
import ma.cimr.agmbackend.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
		return ResponseEntity.ok(authService.login(userLoginRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<UserLoginResponse> renewAccessTokenUsingRefreshToken(
			@RequestBody TokenRefreshRequest tokenRefreshRequest) {
		return ResponseEntity.ok(authService.renewAccessTokenUsingRefreshToken(tokenRefreshRequest));
	}
}

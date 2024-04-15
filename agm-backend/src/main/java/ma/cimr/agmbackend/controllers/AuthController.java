package ma.cimr.agmbackend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.TokenRefreshReqDTO;
import ma.cimr.agmbackend.dto.UserCreationReqDTO;
import ma.cimr.agmbackend.dto.UserLoginReqDTO;
import ma.cimr.agmbackend.dto.UserLoginResDTO;
import ma.cimr.agmbackend.services.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<UserLoginResDTO> login(@RequestBody UserLoginReqDTO userLoginRequest) {
		return ResponseEntity.ok(authService.login(userLoginRequest));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<UserLoginResDTO> refreshToken(@RequestBody TokenRefreshReqDTO tokenRefreshReqDTO) {
		return ResponseEntity.ok(authService.refreshToken(tokenRefreshReqDTO));
	}
}

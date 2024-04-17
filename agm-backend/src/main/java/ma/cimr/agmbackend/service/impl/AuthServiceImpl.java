package ma.cimr.agmbackend.service.impl;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.TokenRefreshRequest;
import ma.cimr.agmbackend.dto.request.UserLoginRequest;
import ma.cimr.agmbackend.dto.response.UserLoginResponse;
import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.repository.UserRepository;
import ma.cimr.agmbackend.service.AuthService;
import ma.cimr.agmbackend.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public UserLoginResponse login(UserLoginRequest userLoginRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(),
				userLoginRequest.getPassword()));
		var user = userRepository.findByEmail(userLoginRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
		var accessToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		UserLoginResponse userLoginResponse = new UserLoginResponse();
		userLoginResponse.setAccessToken(accessToken);
		userLoginResponse.setRefreshToken(refreshToken);
		return userLoginResponse;
	}

	public UserLoginResponse renewAccessTokenUsingRefreshToken(TokenRefreshRequest tokenRefreshRequest) {
		String userEmail = jwtService.extractUsername(tokenRefreshRequest.getRefreshToken());
		User user = userRepository.findByEmail(userEmail).orElseThrow();
		if (jwtService.isTokenValid(tokenRefreshRequest.getRefreshToken(), user)) {
			var accessToken = jwtService.generateToken(user);
			UserLoginResponse userLoginResponse = new UserLoginResponse();
			userLoginResponse.setAccessToken(accessToken);
			userLoginResponse.setRefreshToken(tokenRefreshRequest.getRefreshToken());
			return userLoginResponse;
		}
		return null;
	}
}

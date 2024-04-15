package ma.cimr.agmbackend.services.impl;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.TokenRefreshReqDTO;
import ma.cimr.agmbackend.dto.UserLoginReqDTO;
import ma.cimr.agmbackend.dto.UserLoginResDTO;
import ma.cimr.agmbackend.models.User;
import ma.cimr.agmbackend.repositories.UserRepository;
import ma.cimr.agmbackend.services.AuthService;
import ma.cimr.agmbackend.services.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	public UserLoginResDTO login(UserLoginReqDTO userLoginRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(),
				userLoginRequest.getPassword()));
		var user = userRepository.findByEmail(userLoginRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
		var accessToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		UserLoginResDTO userLoginResponse = new UserLoginResDTO();
		userLoginResponse.setAccessToken(accessToken);
		userLoginResponse.setRefreshToken(refreshToken);
		return userLoginResponse;
	}

	public UserLoginResDTO refreshToken(TokenRefreshReqDTO tokenRefreshReqDTO) {
		String userEmail = jwtService.extractUsername(tokenRefreshReqDTO.getRefreshToken());
		User user = userRepository.findByEmail(userEmail).orElseThrow();
		if (jwtService.isTokenValid(tokenRefreshReqDTO.getRefreshToken(), user)) {
			var accessToken = jwtService.generateToken(user);
			UserLoginResDTO userLoginResponse = new UserLoginResDTO();
			userLoginResponse.setAccessToken(accessToken);
			userLoginResponse.setRefreshToken(tokenRefreshReqDTO.getRefreshToken());
			return userLoginResponse;
		}
		return null;
	}
}

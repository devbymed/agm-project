package ma.cimr.agmbackend.auth;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthResponse authenticateUser(AuthRequest authRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
				authRequest.getPassword()));
		var user = userRepository.findByEmail(authRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
		var accessToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setAccessToken(accessToken);
		authResponse.setRefreshToken(refreshToken);
		return authResponse;
	}

	public AuthResponse renewAccessToken(TokenRefreshRequest tokenRefreshRequest) {
		String userEmail = jwtService.extractUsername(tokenRefreshRequest.getRefreshToken());
		User user = userRepository.findByEmail(userEmail).orElseThrow();
		if (jwtService.isTokenValid(tokenRefreshRequest.getRefreshToken(), user)) {
			var accessToken = jwtService.generateToken(user);
			AuthResponse authResponse = new AuthResponse();
			authResponse.setAccessToken(accessToken);
			authResponse.setRefreshToken(tokenRefreshRequest.getRefreshToken());
			return authResponse;
		}
		return null;
	}
}

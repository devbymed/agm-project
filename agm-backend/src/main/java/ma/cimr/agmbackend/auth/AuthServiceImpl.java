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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.email(),
                authRequest.password()));
        var user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(BadCredentialsApiException::new);
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse renewAccessToken(TokenRefreshRequest tokenRefreshRequest) {
        String email = jwtService.extractUsername(tokenRefreshRequest.refreshToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        if (jwtService.isTokenValid(tokenRefreshRequest.refreshToken(), user)) {
            var accessToken = jwtService.generateToken(user);
            return new AuthResponse(accessToken, tokenRefreshRequest.refreshToken());
        }
        throw new InvalidTokenException();
    }
}

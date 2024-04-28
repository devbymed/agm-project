package ma.cimr.agmbackend.auth;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.email(),
                authRequest.password()));
        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new ApiException(ApiExceptionCodes.BAD_CREDENTIALS));
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<String, Object>(), user);
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse renewAccessToken(TokenRefreshRequest tokenRefreshRequest) {
        String email = jwtService.extractUsername(tokenRefreshRequest.refreshToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        if (jwtService.isTokenValid(tokenRefreshRequest.refreshToken(), user)) {
            String accessToken = jwtService.generateToken(user);
            return new AuthResponse(accessToken, tokenRefreshRequest.refreshToken());
        }
        throw new ApiException(ApiExceptionCodes.INVALID_TOKEN);
    }
}

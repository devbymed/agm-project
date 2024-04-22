package ma.cimr.agmbackend.auth;

public interface AuthService {

	AuthResponse login(AuthRequest authRequest);

	AuthResponse renewAccessToken(TokenRefreshRequest tokenRefreshRequest);
}

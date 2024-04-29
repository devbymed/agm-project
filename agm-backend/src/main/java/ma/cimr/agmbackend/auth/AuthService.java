package ma.cimr.agmbackend.auth;

public interface AuthService {

	AuthResponse authenticateUser(AuthRequest authRequest);

	// AuthResponse renewAccessToken(TokenRefreshRequest tokenRefreshRequest);

	void changePassword(ChangePasswordRequest changePasswordRequest);
}

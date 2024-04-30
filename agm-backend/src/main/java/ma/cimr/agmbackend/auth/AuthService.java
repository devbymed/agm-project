package ma.cimr.agmbackend.auth;

public interface AuthService {

	AuthResponse authenticateUser(AuthRequest authRequest);

	void changePassword(ChangePasswordRequest changePasswordRequest);
}

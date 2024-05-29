package ma.cimr.agmbackend.auth;

import java.security.Principal;

public interface AuthService {

	AuthResponse authenticateUser(AuthRequest authRequest);

	void changePassword(ChangePasswordRequest changePasswordRequest, Principal authenticatedUser);
}

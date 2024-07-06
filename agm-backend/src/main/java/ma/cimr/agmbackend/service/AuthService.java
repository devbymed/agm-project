package ma.cimr.agmbackend.service;

import java.security.Principal;

import ma.cimr.agmbackend.dto.request.AuthRequest;
import ma.cimr.agmbackend.dto.request.ChangePasswordRequest;
import ma.cimr.agmbackend.dto.response.AuthResponse;

public interface AuthService {

	AuthResponse authenticateUser(AuthRequest authRequest);

	void changePassword(ChangePasswordRequest changePasswordRequest, Principal authenticatedUser);
}

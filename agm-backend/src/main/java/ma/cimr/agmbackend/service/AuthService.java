package ma.cimr.agmbackend.service;

import ma.cimr.agmbackend.dto.request.TokenRefreshRequest;
import ma.cimr.agmbackend.dto.request.UserLoginRequest;
import ma.cimr.agmbackend.dto.response.UserLoginResponse;

public interface AuthService {

	UserLoginResponse login(UserLoginRequest userLoginRequest);

	UserLoginResponse renewAccessTokenUsingRefreshToken(TokenRefreshRequest tokenRefreshRequest);
}

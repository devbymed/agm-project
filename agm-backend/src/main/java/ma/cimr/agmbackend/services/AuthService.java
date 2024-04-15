package ma.cimr.agmbackend.services;

import ma.cimr.agmbackend.dto.TokenRefreshReqDTO;
import ma.cimr.agmbackend.dto.UserLoginReqDTO;
import ma.cimr.agmbackend.dto.UserLoginResDTO;

public interface AuthService {

	UserLoginResDTO login(UserLoginReqDTO userLoginRequest);

	UserLoginResDTO refreshToken(TokenRefreshReqDTO tokenRefreshReqDTO);
}

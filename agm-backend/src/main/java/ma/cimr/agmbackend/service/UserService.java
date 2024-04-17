package ma.cimr.agmbackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ma.cimr.agmbackend.dto.request.UserCreateRequest;
import ma.cimr.agmbackend.dto.response.UserCreateResponse;

public interface UserService {

	UserDetailsService userDetailsService();

	UserCreateResponse createUser(UserCreateRequest userCreationRequest);
}

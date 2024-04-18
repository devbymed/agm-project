package ma.cimr.agmbackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ma.cimr.agmbackend.dto.request.UserCreateRequest;
import ma.cimr.agmbackend.dto.response.UserCreateResponse;
import ma.cimr.agmbackend.model.User;

public interface UserService {

	UserDetailsService userDetailsService();

	User createUser(UserCreateRequest userCreationRequest);
}

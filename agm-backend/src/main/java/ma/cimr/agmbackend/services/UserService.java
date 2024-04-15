package ma.cimr.agmbackend.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import ma.cimr.agmbackend.dto.UserCreationRequestDTO;
import ma.cimr.agmbackend.models.User;

public interface UserService {

	UserDetailsService userDetailsService();

	User createUser(UserCreationRequestDTO userCreationRequest);
}

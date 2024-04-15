package ma.cimr.agmbackend.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import ma.cimr.agmbackend.dto.UserCreationReqDTO;
import ma.cimr.agmbackend.models.User;

public interface UserService {

	UserDetailsService userDetailsService();

	User createUser(UserCreationReqDTO userCreationRequest);
}

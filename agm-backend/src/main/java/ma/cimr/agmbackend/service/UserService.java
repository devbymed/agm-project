package ma.cimr.agmbackend.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.mail.MessagingException;
import ma.cimr.agmbackend.dto.request.UserAddRequest;
import ma.cimr.agmbackend.dto.request.UserEditRequest;
import ma.cimr.agmbackend.dto.response.UserResponse;

public interface UserService {

	UserDetailsService userDetailsService();

	List<UserResponse> getUsers();

	UserResponse getUser(Long id);

	UserResponse createUser(UserAddRequest userAddRequest) throws MessagingException;

	UserResponse updateUser(Long id, UserEditRequest userEditRequest);

	void deleteUser(Long id);
}

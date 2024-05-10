package ma.cimr.agmbackend.user;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.mail.MessagingException;

public interface UserService {

	UserDetailsService userDetailsService();

	Page<UserResponse> getUsers(int page, int size);

	UserResponse getUser(Long id);

	UserResponse createUser(UserAddRequest userAddRequest) throws MessagingException;

	UserResponse updateUser(Long id, UserEditRequest userEditRequest);

	void deleteUser(Long id);
}

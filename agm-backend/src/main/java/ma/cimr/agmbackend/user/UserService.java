package ma.cimr.agmbackend.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

	UserDetailsService userDetailsService();

	List<UserResponse> getUsers();

	UserResponse getUser(Long id);

	UserResponse createUser(UserAddRequest userAddRequest);

	UserResponse updateUser(Long id, UserEditRequest userEditRequest);

	void deleteUser(Long id);
}

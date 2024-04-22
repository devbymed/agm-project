package ma.cimr.agmbackend.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

	UserDetailsService userDetailsService();

	UserResponse createUser(UserAddRequest userAddRequest);

	UserResponse updateUser(Long id, UserEditRequest userEditRequest);
}

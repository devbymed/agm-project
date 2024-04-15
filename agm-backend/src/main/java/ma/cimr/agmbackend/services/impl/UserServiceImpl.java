package ma.cimr.agmbackend.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.UserCreationRequestDTO;
import ma.cimr.agmbackend.models.User;
import ma.cimr.agmbackend.models.enums.Role;
import ma.cimr.agmbackend.repositories.UserRepository;
import ma.cimr.agmbackend.services.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				return userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
			}
		};
	}

	public User createUser(UserCreationRequestDTO userCreationRequest) {
		User user = new User();
		user.setFirstName(userCreationRequest.getFirstName());
		user.setLastName(userCreationRequest.getLastName());
		user.setRole(Role.USER);
		user.setEmail(userCreationRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
		return userRepository.save(user);
	}
}

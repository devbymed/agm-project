package ma.cimr.agmbackend.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.UserCreationReqDTO;
import ma.cimr.agmbackend.models.User;
import ma.cimr.agmbackend.models.enums.Role;
import ma.cimr.agmbackend.repositories.UserRepository;
import ma.cimr.agmbackend.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
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

	public User createUser(UserCreationReqDTO userCreationRequest) {
		User user = new User();
		user.setFirstName(userCreationRequest.getFirstName());
		user.setLastName(userCreationRequest.getLastName());
		user.setRole(Role.USER);
		user.setEmail(userCreationRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
		user.setCreatedAt(LocalDateTime.now());
		return userRepository.save(user);
	}
}

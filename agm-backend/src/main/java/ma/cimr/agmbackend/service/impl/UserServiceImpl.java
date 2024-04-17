package ma.cimr.agmbackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ma.cimr.agmbackend.dto.request.UserCreateRequest;
import ma.cimr.agmbackend.dto.response.UserCreateResponse;
import ma.cimr.agmbackend.mapper.UserMapper;
import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.repository.UserRepository;
import ma.cimr.agmbackend.service.UserService;
import ma.cimr.agmbackend.util.RandomStringGenerator;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userMapper = userMapper;
	}

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

	public UserCreateResponse createUser(UserCreateRequest userCreateRequest) {
		// User user = new User();
		// user.setFirstName(userCreateRequest.getFirstName());
		// user.setLastName(userCreateRequest.getLastName());
		// user.setRole(Role.USER);
		// user.setEmail(userCreateRequest.getEmail());
		// user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));Ou
		// return userRepository.save(user);
		String generatedPassword = RandomStringGenerator.generateSecurePassword(12);
		LOGGER.info(String.format("* Generated password for %s %s: %s",
				userCreateRequest.getFirstName(), userCreateRequest.getLastName(), generatedPassword));
		User user = userMapper.toUser(userCreateRequest);
		user.setPassword(passwordEncoder.encode(generatedPassword));
		User userSaved = userRepository.save(user);
		return userMapper.toUserCreateResponse(userSaved);
	}
}

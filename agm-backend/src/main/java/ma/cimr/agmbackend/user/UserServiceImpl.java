package ma.cimr.agmbackend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Validator;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.profile.ProfileNotFoundException;
import ma.cimr.agmbackend.profile.ProfileRepository;
import ma.cimr.agmbackend.util.SecurePasswordGenerator;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, ProfileRepository profileRepository,
			@Lazy PasswordEncoder passwordEncoder, UserMapper userMapper, Validator validator) {
		this.userRepository = userRepository;
		this.profileRepository = profileRepository;
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

	public UserResponse createUser(UserAddRequest userCreateRequest) {
		String generatedPassword = SecurePasswordGenerator.generateSecurePassword(12);
		LOGGER.info(String.format("* Generated password for %s %s: %s", userCreateRequest.getFirstName(),
				userCreateRequest.getLastName(), generatedPassword));
		User user = userMapper.toUser(userCreateRequest);
		user.setPassword(passwordEncoder.encode(generatedPassword));
		user.setProfile(profileRepository.findById(userCreateRequest.getProfileId())
				.orElseThrow(() -> new IllegalArgumentException("Profile not found")));
		userRepository.save(user);
		return userMapper.toUserResponse(user);
	}

	public UserResponse updateUser(Long id, UserEditRequest userEditRequest) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(ApiExceptionCodes.USER_NOT_FOUND));
		if (userEditRequest.getFirstName() != null) {
			user.setFirstName(userEditRequest.getFirstName());
		}
		if (userEditRequest.getLastName() != null) {
			user.setLastName(userEditRequest.getLastName());
		}
		if (userEditRequest.getProfileId() != null) {
			user.setProfile(profileRepository.findById(userEditRequest.getProfileId())
					.orElseThrow(() -> new ProfileNotFoundException(ApiExceptionCodes.PROFILE_NOT_FOUND)));
		}
		userRepository.save(user);
		return userMapper.toUserResponse(user);
	}
}

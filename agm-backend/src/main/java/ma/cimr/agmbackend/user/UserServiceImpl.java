package ma.cimr.agmbackend.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
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
            @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
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
                        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
            }
        };
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    public UserResponse getUser(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponse)
                .orElseThrow(() -> new ApiException(ApiExceptionCodes.USER_NOT_FOUND));
    }

    @Override
    public UserResponse createUser(UserAddRequest userCreateRequest) {
        String generatedPassword = SecurePasswordGenerator.generateSecurePassword(12);
        LOGGER.info(String.format("* Generated password for %s %s: %s", userCreateRequest.firstName(),
                userCreateRequest.lastName(), generatedPassword));
        User user = userMapper.toUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setProfile(
                profileRepository.findById(userCreateRequest.profileId())
                        .orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND)));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserEditRequest userEditRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ApiExceptionCodes.USER_NOT_FOUND));
        Optional.ofNullable(userEditRequest.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userEditRequest.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(userEditRequest.profileId()).ifPresent(profileId -> user
                .setProfile(profileRepository.findById(profileId)
                        .orElseThrow(() -> new ApiException(ApiExceptionCodes.PROFILE_NOT_FOUND))));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ApiExceptionCodes.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}

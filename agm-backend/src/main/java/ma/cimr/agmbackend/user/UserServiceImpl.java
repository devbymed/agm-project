package ma.cimr.agmbackend.user;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.EMAIL_ALREADY_EXISTS;
import static ma.cimr.agmbackend.exception.ApiExceptionCodes.PROFILE_NOT_FOUND;
import static ma.cimr.agmbackend.exception.ApiExceptionCodes.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import ma.cimr.agmbackend.email.EmailService;
import ma.cimr.agmbackend.email.EmailTemplateName;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.profile.ProfileRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, ProfileRepository profileRepository,
            @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                Hibernate.initialize(user.getProfile().getPermissions());
                // user.getProfile().getPermissions()
                // .forEach(permission -> LOGGER.info("Permission: " + permission.getName()));
                return user;
            }
        };
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseWithoutPermissions)
                .toList();
    }

    @Override
    public UserResponse getUser(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponseWithoutPermissions)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
    }

    @Override
    public UserResponse createUser(UserAddRequest userCreateRequest) throws MessagingException {
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new ApiException(EMAIL_ALREADY_EXISTS);
        }
        String generatedPassword = "password";
        // String generatedPassword = SecurePasswordGenerator.generateSecurePassword(8);
        LOGGER.info(String.format("* Generated password for %s %s: %s", userCreateRequest.getFirstName(),
                userCreateRequest.getLastName(), generatedPassword));
        User user = userMapper.toUser(userCreateRequest);
        user.setUsername(userCreateRequest.getEmail().split("@")[0]);
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setProfile(
                profileRepository.findById(userCreateRequest.getProfileId())
                        .orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND)));
        userRepository.save(user);
        sendWelcomeEmail(user, generatedPassword);
        return userMapper.toUserResponseWithoutPermissions(user);
    }

    private void sendWelcomeEmail(User user, String generatedPassword) throws MessagingException {
        LOGGER.info(String.format("* Email sent to %s %s: %s", user.getFirstName(),
                user.getLastName(), user.getEmail()));
        emailService.sendEmail(user.getEmail(), user.getFirstName(),
                "Bienvenue sur l'application de gestion des assemblées générales de la CIMR", user.getUsername(),
                generatedPassword, EmailTemplateName.NEW_USER);
    }

    @Override
    public UserResponse updateUser(Long id, UserEditRequest userEditRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Optional.ofNullable(userEditRequest.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(userEditRequest.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(userEditRequest.getProfileId()).ifPresent(profileId -> user
                .setProfile(profileRepository.findById(profileId)
                        .orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND))));
        userRepository.save(user);
        return userMapper.toUserResponseWithoutPermissions(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public List<UserResponse> getAgents() {
        List<User> agents = userRepository.findAllByProfileName("Agent de relance");
        return agents.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}

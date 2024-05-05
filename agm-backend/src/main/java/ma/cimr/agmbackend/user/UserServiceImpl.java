package ma.cimr.agmbackend.user;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.PROFILE_NOT_FOUND;
import static ma.cimr.agmbackend.exception.ApiExceptionCodes.USER_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import ma.cimr.agmbackend.util.SecurePasswordGenerator;

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
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

                List<GrantedAuthority> authorities = user.getProfile().getFeatures().stream()
                        .map(feature -> new SimpleGrantedAuthority(feature.name()))
                        .collect(Collectors.toList());

                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                        authorities);
            }
        };
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponseWithoutFeatures)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(Long id) {
        return userRepository.findById(id).map(userMapper::toUserResponseWithoutFeatures)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
    }

    @Override
    public UserResponse createUser(UserAddRequest userCreateRequest) throws MessagingException {
        String generatedPassword = SecurePasswordGenerator.generateSecurePassword(12);
        LOGGER.info(String.format("* Generated password for %s %s: %s", userCreateRequest.getFirstName(),
                userCreateRequest.getLastName(), generatedPassword));
        User user = userMapper.toUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setProfile(
                profileRepository.findById(userCreateRequest.getProfileId())
                        .orElseThrow(() -> new ApiException(PROFILE_NOT_FOUND)));
        userRepository.save(user);
        sendWelcomeEmail(user, generatedPassword);
        return userMapper.toUserResponseWithoutFeatures(user);
    }

    @SuppressWarnings("unused")
    private void sendWelcomeEmail(User user, String generatedPassword) throws MessagingException {
        LOGGER.info(String.format("* Email sent to %s %s: %s", user.getFirstName(),
                user.getLastName(), user.getEmail()));
        emailService.sendEmail(user.getEmail(),
                " Bienvenue sur l'application de gestion des assemblées générales de la CIMR", user.getFirstName(),
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
        return userMapper.toUserResponseWithoutFeatures(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        userRepository.delete(user);
    }
}

package ma.cimr.agmbackend.auth;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.BAD_CREDENTIALS;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.user.User;
import ma.cimr.agmbackend.user.UserMapper;
import ma.cimr.agmbackend.user.UserRepository;
import ma.cimr.agmbackend.user.UserResponse;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse authenticateUser(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ApiException(BAD_CREDENTIALS));
        String token = jwtService.generateToken(user);
        boolean mustChangePassword = user.isFirstLogin();
        LoginUserResponse userResponse = userMapper.toLoginUserResponse(user);
        return AuthResponse.builder()
                .accessToken(token)
                .mustChangePassword(mustChangePassword)
                .user(userResponse)
                .build();
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiExceptionCodes.USER_NOT_FOUND));
        if (!passwordEncoder.matches(changePasswordRequest.getTemporaryPassword(), user.getPassword())) {
            throw new ApiException(ApiExceptionCodes.OLD_PASSWORD_INCORRECT);
        }
        if (!isValidPassword(changePasswordRequest.getNewPassword())) {
            throw new ApiException(ApiExceptionCodes.INVALID_NEW_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&*()_+\\-=\\[\\]?]).{8,20}$";
        return password.matches(passwordRegex);
    }
}

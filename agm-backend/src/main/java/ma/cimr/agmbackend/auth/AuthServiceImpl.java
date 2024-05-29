package ma.cimr.agmbackend.auth;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.BAD_CREDENTIALS;

import java.security.Principal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public AuthResponse authenticateUser(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ApiException(BAD_CREDENTIALS));
        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return AuthResponse.builder()
                .accessToken(token)
                .firstLogin(user.isFirstLogin())
                .user(userResponse)
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, Principal authenticatedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) authenticatedUser).getPrincipal();
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new ApiException(ApiExceptionCodes.INCORRECT_CURRENT_PASSWORD);
        }
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new ApiException(ApiExceptionCodes.SAME_AS_CURRENT_PASSWORD);
        }
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new ApiException(ApiExceptionCodes.MISMATCHED_PASSWORDS);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);
    }
}

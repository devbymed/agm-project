package ma.cimr.agmbackend.service.impl;

import static ma.cimr.agmbackend.exception.ApiExceptionCodes.BAD_CREDENTIALS;

import java.security.Principal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.cimr.agmbackend.dto.request.AuthRequest;
import ma.cimr.agmbackend.dto.request.ChangePasswordRequest;
import ma.cimr.agmbackend.dto.response.AuthResponse;
import ma.cimr.agmbackend.dto.response.UserResponse;
import ma.cimr.agmbackend.exception.ApiException;
import ma.cimr.agmbackend.exception.ApiExceptionCodes;
import ma.cimr.agmbackend.mapper.UserMapper;
import ma.cimr.agmbackend.model.User;
import ma.cimr.agmbackend.repository.UserRepository;
import ma.cimr.agmbackend.service.AuthService;
import ma.cimr.agmbackend.service.JwtService;

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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                authRequest.getPassword()));
        User user = userRepository.findByUsername(authRequest.getUsername())
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

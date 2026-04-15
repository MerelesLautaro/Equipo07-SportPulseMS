package com.Equipo07_SportPulseMS.ms_auth.service.authentication.impl;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserLoginRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserLoginResponse;
import com.Equipo07_SportPulseMS.ms_auth.entity.Role;
import com.Equipo07_SportPulseMS.ms_auth.entity.User;
import com.Equipo07_SportPulseMS.ms_auth.exception.EmailAlreadyExistsException;
import com.Equipo07_SportPulseMS.ms_auth.exception.InvalidCredentialsException;
import com.Equipo07_SportPulseMS.ms_auth.exception.UsernameAlreadyExistsException;
import com.Equipo07_SportPulseMS.ms_auth.mapper.UserMapper;
import com.Equipo07_SportPulseMS.ms_auth.repository.UserRepository;
import com.Equipo07_SportPulseMS.ms_auth.security.userdetails.CustomUserDetails;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.AuthenticationService;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    @Transactional
    public UserDetailsResponse registerUser(UserRegisterRequest userRegisterRequest) {
        validateEmailAndUsername(userRegisterRequest);

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.password());

        User user = buildUser(userRegisterRequest,encodedPassword);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        try {
            Authentication authentication = authenticateUser(userLoginRequest);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String token = tokenService.generateToken(userDetails);

            return buildLoginResponse(userDetails, token);

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }

    private Authentication authenticateUser(UserLoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
    }

    private UserLoginResponse buildLoginResponse(CustomUserDetails userDetails, String token) {
        return new UserLoginResponse(
                token,
                "Bearer",
                tokenService.getExpirationTime(),
                userDetails.getUser().getId()
        );
    }

    private void validateEmailAndUsername(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmailIgnoreCase(userRegisterRequest.email())) {
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.existsByUsernameIgnoreCase(userRegisterRequest.username())) {
            throw new UsernameAlreadyExistsException();
        }
    }

    private User buildUser(UserRegisterRequest req, String encodedPassword) {

        User user = new User();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);

        return user;
    }
}

package com.Equipo07_SportPulseMS.ms_auth.service.authentication.impl;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.entity.Role;
import com.Equipo07_SportPulseMS.ms_auth.entity.User;
import com.Equipo07_SportPulseMS.ms_auth.exception.EmailAlreadyExistsException;
import com.Equipo07_SportPulseMS.ms_auth.exception.UsernameAlreadyExistsException;
import com.Equipo07_SportPulseMS.ms_auth.mapper.UserMapper;
import com.Equipo07_SportPulseMS.ms_auth.repository.UserRepository;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDetailsResponse registerUser(UserRegisterRequest userRegisterRequest) {
        validateEmailAndUsername(userRegisterRequest);

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.password());

        User user = buildUser(userRegisterRequest,encodedPassword);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
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

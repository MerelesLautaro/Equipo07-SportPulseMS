package com.Equipo07_SportPulseMS.ms_auth.service.authentication;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserLoginRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserLoginResponse;

public interface AuthenticationService {
    UserDetailsResponse registerUser(UserRegisterRequest userRegisterRequest);
    UserLoginResponse login(UserLoginRequest userLoginRequest);
}

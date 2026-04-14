package com.Equipo07_SportPulseMS.ms_auth.mapper;

import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailsResponse toResponse(User user);
}
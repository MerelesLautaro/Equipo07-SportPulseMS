package com.Equipo07_SportPulseMS.ms_auth.integration.authentication;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserLoginRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.entity.Role;
import com.Equipo07_SportPulseMS.ms_auth.entity.User;
import com.Equipo07_SportPulseMS.ms_auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String email;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        email = "test_" + UUID.randomUUID() + "@mail.com";

        createTestUser(email);
    }

    private void createTestUser(String email) {
        User user = new User();
        user.setUsername("javier");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("SportPass123!"));
        user.setRole(Role.USER);

        userRepository.save(user);
    }


    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest(
                "javier_ruiz",
                "Sportpass1",
                "new@email.com"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("javier_ruiz"))
                .andExpect(jsonPath("$.email").value("new@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldFailWhenPasswordIsInvalid() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest(
                "javier_ruiz",
                "short",
                "invalid@email.com"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() throws Exception {

        UserRegisterRequest request = new UserRegisterRequest(
                "javier_ruiz",
                "Sportpass1",
                email
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("USER_ALREADY_EXISTS"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        UserLoginRequest request = new UserLoginRequest(
                email,
                "SportPass123!"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").exists())
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {

        UserLoginRequest request = new UserLoginRequest(
                email,
                "wrongpassword"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_CREDENTIALS"));
    }
}
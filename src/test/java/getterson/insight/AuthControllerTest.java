package getterson.insight;

import com.fasterxml.jackson.databind.ObjectMapper;
import getterson.insight.controllers.AuthController;
import getterson.insight.dtos.LoginRequestDTO;
import getterson.insight.dtos.RegisterRequestDTO;
import getterson.insight.entities.UserEntity;
import getterson.insight.exceptions.user.DuplicatedUserException;
import getterson.insight.services.JWTService;
import getterson.insight.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityTestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        Mockito.reset(userService, jwtService);
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testuser", "password123");
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("testuser");

        when(userService.validateUserLogin(eq("testuser"), eq("password123")))
                .thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("mock-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testLogin_Failure_InvalidCredentials() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testuser", "wrongpassword");

        when(userService.validateUserLogin(eq("testuser"), eq("wrongpassword")))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Usuário ou senha incorretos"));
    }

    @Test
    void testRegister_Success() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "John Doe", "johndoe", "12345678901", "01/01/1990",
                "johndoe@example.com", "password123", "1234567890"
        );
        UserEntity mockUser = new UserEntity();
        mockUser.setUsername("johndoe");

        when(userService.registerUser(
                anyString(), anyString(), anyString(), any(), anyString(), anyString(), anyString()
        )).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn("mock-token");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"))
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    void testRegister_Failure_DuplicatedUser() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "John Doe", "johndoe", "12345678901", "01/01/1990",
                "johndoe@example.com", "password123", "1234567890"
        );

        when(userService.registerUser(
                anyString(), anyString(), anyString(), any(), anyString(), anyString(), anyString()
        )).thenThrow(new DuplicatedUserException("Usuário já existe"));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Usuário já existe"));
    }
}

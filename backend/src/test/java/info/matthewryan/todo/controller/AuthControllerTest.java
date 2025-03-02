package info.matthewryan.todo.controller;

import info.matthewryan.todo.security.AuthRequest;
import info.matthewryan.todo.security.JwtUtil;
import info.matthewryan.todo.security.CustomUserDetailsService;
import info.matthewryan.todo.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class) // ✅ Import SecurityConfig to include PasswordEncoder bean
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ Ensure PasswordEncoder is available

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new User("admin", passwordEncoder.encode("admin"), Collections.emptyList()); // ✅ Encode password
    }

    @Test
    void testLoginSuccess() throws Exception {
        AuthRequest request = new AuthRequest("admin", "admin");

        Mockito.when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        Mockito.when(jwtUtil.generateToken("admin")).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\": \"admin\", \"password\": \"admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testInvalidLogin() throws Exception {
        Mockito.when(userDetailsService.loadUserByUsername("admin")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\": \"admin\", \"password\": \"wrong-password\"}"))
                .andExpect(status().isUnauthorized());
    }
}

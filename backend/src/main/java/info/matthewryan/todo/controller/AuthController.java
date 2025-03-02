package info.matthewryan.todo.controller;

import info.matthewryan.todo.security.AuthRequest;
import info.matthewryan.todo.security.CustomUserDetailsService;
import info.matthewryan.todo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // ✅ Add PasswordEncoder

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) { // ✅ Inject PasswordEncoder
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder; // ✅ Assign PasswordEncoder
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        System.out.println("🔹 Login Attempt: " + authRequest.getUsername() + " / " + authRequest.getPassword());

        // ✅ Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // ✅ Compare raw password with encoded password
        if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // ✅ Generate JWT Token
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok().body(Map.of("token", jwtToken));
    }
}

package info.matthewryan.todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("admin");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("admin");
        assertEquals("admin", jwtUtil.extractUsername(token));
    }

    @Test
    void testValidateToken() {
        UserDetails userDetails = new User("admin", "password", Collections.emptyList());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testExpiredToken() {
        // 🛠 Create a token that expired 2 minutes ago
        String expiredToken = Jwts.builder()
                .setSubject("admin")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 12)) // Issued 12 min ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 2)) // Expired 2 min ago
                .signWith(jwtUtil.getSecretKey())
                .compact();

        UserDetails userDetails = new User("admin", "password", Collections.emptyList());

        try {
            boolean isValid = jwtUtil.validateToken(expiredToken, userDetails);
            assertFalse(isValid, "Expired token should be invalid");
        } catch (ExpiredJwtException e) {
            // ✅ Expected: JWT should be expired
            System.out.println("✅ Token is expired as expected.");
        }
    }


    @Test
    void testValidToken() {
        // ✅ Token expires in 1 hour
        String validToken = Jwts.builder()
                .setSubject("admin")
                .setIssuedAt(new Date(System.currentTimeMillis())) // Issued now
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expires in 1 hour
                .signWith(jwtUtil.getSecretKey())
                .compact();

        UserDetails userDetails = new User("admin", "password", Collections.emptyList());

        assertTrue(jwtUtil.validateToken(validToken, userDetails)); // ✅ Should return true
    }


    @Test
    void testInvalidToken() {
        String fakeToken = "invalid.jwt.token";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(fakeToken));
    }
}

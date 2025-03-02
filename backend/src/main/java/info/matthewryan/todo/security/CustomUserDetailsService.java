package info.matthewryan.todo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final Map<String, UserDetails> users = new ConcurrentHashMap<>();

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        // ✅ Ensure password is ENCODED before storing
        String encodedPassword = passwordEncoder.encode("admin");

        users.put("admin", User.withUsername("admin")
                .password(encodedPassword) // 🔥 Use encoded password
                .roles("ADMIN")
                .build());

        System.out.println("✅ Stored Admin Password (Encoded): " + encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }
}

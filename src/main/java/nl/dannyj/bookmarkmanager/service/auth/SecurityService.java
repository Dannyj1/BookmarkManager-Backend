package nl.dannyj.bookmarkmanager.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean verifyPassword(String password, String passwordHash) {
        return passwordEncoder.matches(password, passwordHash);
    }
}

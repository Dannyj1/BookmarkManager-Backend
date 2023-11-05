package nl.dannyj.bookmarkmanager.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC512(secret);
    }
}

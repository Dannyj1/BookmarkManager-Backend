package nl.dannyj.bookmarkmanager.service.auth;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    private final Algorithm jwtAlgorithm;

    @Autowired
    public JWTService(Algorithm jwtAlgorithm) {
        this.jwtAlgorithm = jwtAlgorithm;
    }
}

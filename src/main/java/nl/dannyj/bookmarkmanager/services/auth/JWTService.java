/*
 A modern, archiving bookmark manager.
 Copyright (C) 2023  Danny Jelsma

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nl.dannyj.bookmarkmanager.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.dannyj.bookmarkmanager.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class JWTService {

    private final Algorithm jwtAlgorithm;
    private final JWTVerifier verifier;

    @Autowired
    public JWTService(Algorithm jwtAlgorithm) {
        this.jwtAlgorithm = jwtAlgorithm;
        this.verifier = JWT.require(jwtAlgorithm).build();
    }

    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<DecodedJWT> decodeToken(String token) {
        try {
            return Optional.of(verifier.verify(token));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String generateToken(User user, boolean remember) {
        String userId = Integer.toString(user.getId());
        Instant expirationTime = Instant.now().plus(Duration.ofHours(12));

        if (remember) {
            expirationTime = Instant.now().plus(Duration.ofDays(30));
        }

        return JWT.create()
                .withSubject(userId)
                .withClaim("username", user.getUsername())
                .withExpiresAt(expirationTime)
                .withIssuedAt(Instant.now())
                .sign(jwtAlgorithm);
    }
}

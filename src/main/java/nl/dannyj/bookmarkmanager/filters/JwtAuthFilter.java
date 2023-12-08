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

package nl.dannyj.bookmarkmanager.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import nl.dannyj.bookmarkmanager.models.User;
import nl.dannyj.bookmarkmanager.services.UserService;
import nl.dannyj.bookmarkmanager.services.auth.JWTService;
import nl.dannyj.bookmarkmanager.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    @Autowired
    public JwtAuthFilter(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<DecodedJWT> optToken = decodeJwtToken(request, response);

        if (optToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        DecodedJWT jwt = optToken.get();
        String subject = jwt.getSubject();
        Optional<User> optUser = getUser(subject);

        if (optUser.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = optUser.get();
        Set<SimpleGrantedAuthority> authorities = getAuthorities(user);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(subject, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }

    private static Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        return authorities;
    }

    private Optional<User> getUser(String subject) {
        if (!NumberUtil.isInteger(subject)) {
            return Optional.empty();
        }

        int userId = Integer.parseInt(subject);
        return userService.getUserById(userId);
    }

    private Optional<DecodedJWT> decodeJwtToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        String headerPrefix = "Bearer ";

        if (authHeader == null || !authHeader.startsWith(headerPrefix)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return Optional.empty();
        }

        int headerPrefixLength = headerPrefix.length();
        String token = authHeader.substring(headerPrefixLength);
        
        return jwtService.decodeToken(token);
    }
}

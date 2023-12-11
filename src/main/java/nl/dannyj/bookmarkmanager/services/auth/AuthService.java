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

import lombok.NonNull;
import nl.dannyj.bookmarkmanager.dtos.auth.AuthTokenDTO;
import nl.dannyj.bookmarkmanager.dtos.auth.LoginRequestDTO;
import nl.dannyj.bookmarkmanager.exceptions.InvalidLoginCredentialsException;
import nl.dannyj.bookmarkmanager.models.User;
import nl.dannyj.bookmarkmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]+$");

    private final PasswordHashService passwordHashService;
    private final UserService userService;
    private final JWTService jwtService;

    @Autowired
    public AuthService(PasswordHashService passwordHashService, UserService userService, JWTService jwtService) {
        this.passwordHashService = passwordHashService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthTokenDTO authenticate(@NonNull LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();

        if (!isUsernameValid(username)) {
            throw new InvalidLoginCredentialsException();
        }

        String password = loginRequest.getPassword();
        boolean remember = loginRequest.isRemember();
        User user = userService.findUserByUsername(username)
                .orElseThrow(InvalidLoginCredentialsException::new);

        if (!passwordHashService.verifyPassword(password, user.getPasswordHash())) {
            throw new InvalidLoginCredentialsException();
        }

        String token = this.jwtService.generateToken(user, remember);
        return new AuthTokenDTO(token);
    }

    private boolean isUsernameValid(@NonNull String username) {
        if (username.length() < 3 || username.length() > 32) {
            return false;
        }

        return USERNAME_REGEX.matcher(username).matches();
    }
}

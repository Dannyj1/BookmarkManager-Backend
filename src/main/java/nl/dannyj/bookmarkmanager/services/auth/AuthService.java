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
import nl.dannyj.bookmarkmanager.validators.Validator;
import nl.dannyj.bookmarkmanager.validators.ValidatorResult;
import nl.dannyj.bookmarkmanager.validators.auth.PasswordValidator;
import nl.dannyj.bookmarkmanager.validators.auth.UsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {

    private final PasswordHashService passwordHashService;
    private final UserService userService;
    private final JWTService jwtService;

    private final Validator<String> usernameValidator;
    private final Validator<String> passwordValidator;

    @Autowired
    public AuthService(PasswordHashService passwordHashService, UserService userService, JWTService jwtService) {
        this.passwordHashService = passwordHashService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.usernameValidator = new UsernameValidator();
        this.passwordValidator = new PasswordValidator();
    }

    // TODO: consider ideas: Checking against HIBP and notifying user (or even forcing password change)
    // TODO: consider ideas: Implement cloudflare's captcha (optional feature)
    public AuthTokenDTO authenticate(@NonNull LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        ValidatorResult<String> usernameValidationResult = usernameValidator.validate(username);

        if (!usernameValidationResult.isValid()) {
            throw new InvalidLoginCredentialsException();
        }

        String password = loginRequest.getPassword();
        ValidatorResult<String> passwordValidationResult = passwordValidator.validate(password);

        if (!passwordValidationResult.isValid()) {
            throw new InvalidLoginCredentialsException();
        }

        boolean remember = loginRequest.isRemember();
        User user = userService.findUserByUsername(username)
                .orElseThrow(InvalidLoginCredentialsException::new);

        if (!passwordHashService.verifyPassword(password, user.getPasswordHash())) {
            throw new InvalidLoginCredentialsException();
        }

        String token = this.jwtService.generateToken(user, remember);
        return new AuthTokenDTO(token);
    }

    // TODO: implement, return something
    // TODO: consider ideas: Checking against HIBP: https://github.com/GideonLeGrange/haveibeenpwned
    public void register () {

    }
}

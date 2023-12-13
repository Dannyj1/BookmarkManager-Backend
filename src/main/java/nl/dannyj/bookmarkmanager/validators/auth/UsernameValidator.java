package nl.dannyj.bookmarkmanager.validators.auth;

import nl.dannyj.bookmarkmanager.validators.Validator;
import nl.dannyj.bookmarkmanager.validators.ValidatorResult;

import java.util.regex.Pattern;

public class UsernameValidator implements Validator<String> {

    private static final Pattern USERNAME_REGEX = Pattern.compile("\\w{3,32}");

    @Override
    public ValidatorResult<String> validate(String username) {
        if (username.length() < 3 || username.length() > 32) {
            return ValidatorResult.invalid("Username must be between 3 and 32 characters long.");
        }

        if (!USERNAME_REGEX.matcher(username).matches()) {
            return ValidatorResult.invalid("Username must only contain letters, numbers and underscores.");
        }

        return ValidatorResult.valid(username);
    }
}

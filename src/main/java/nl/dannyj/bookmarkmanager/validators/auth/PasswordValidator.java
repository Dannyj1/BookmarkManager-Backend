package nl.dannyj.bookmarkmanager.validators.auth;

import nl.dannyj.bookmarkmanager.validators.Validator;
import nl.dannyj.bookmarkmanager.validators.ValidatorResult;

import java.util.regex.Pattern;

public class PasswordValidator implements Validator<String> {

    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[^a-zA-Z0-9]");

    @Override
    public ValidatorResult<String> validate(String password) {
        if (password.length() < 8 || password.length() > 128) {
            return ValidatorResult.invalid("Password must be between 8 and 128 characters long.");
        }

        // Contains at least one lowercase, one uppercase, one number and one special character
        if (!(LOWERCASE.matcher(password).find() && UPPERCASE.matcher(password).find() &&
                DIGIT.matcher(password).find() &&  SPECIAL_CHAR.matcher(password).find())) {
            return ValidatorResult.invalid("Password must contain at least one lowercase, one uppercase, one number and one special character.");
        }

        return ValidatorResult.valid(password);
    }
}

package nl.dannyj.bookmarkmanager.validators;

import lombok.Getter;

@Getter
public class ValidatorResult<T> {

    private final T value;
    private final boolean valid;
    private final String errorMessage;

    private ValidatorResult(T value, boolean valid, String errorMessage) {
        this.value = value;
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public static <T> ValidatorResult<T> valid(T value) {
        return new ValidatorResult<>(value, true, null);
    }

    public static <T> ValidatorResult<T> invalid(String errorMessage) {
        return new ValidatorResult<>(null, false, errorMessage);
    }

    public static <T> ValidatorResult<T> invalid(String errorMessage, Object... args) {
        return new ValidatorResult<>(null, false, String.format(errorMessage, args));
    }
}

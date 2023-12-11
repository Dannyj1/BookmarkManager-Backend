package nl.dannyj.bookmarkmanager.validators;

public interface Validator<T> {

    ValidatorResult<T> isValid(T value);
}

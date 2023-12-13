package nl.dannyj.bookmarkmanager.validators;

public interface Validator<T> {

    ValidatorResult<T> validate(T value);
}

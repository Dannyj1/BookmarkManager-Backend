package nl.dannyj.bookmarkmanager.validators;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class CompositeValidator<T> implements Validator<T> {

    private List<Validator<T>> validators = new ArrayList<>();

    public void addValidator(Validator<T> validator) {
        this.validators.add(validator);
    }

    public void addValidators(List<Validator<T>> validators) {
        this.validators.addAll(validators);
    }

    public void addValidators(Validator<T>... validators) {
        Collections.addAll(this.validators, validators);
    }

    @Override
    public ValidatorResult<T> validate(T value) {
        for (Validator<T> validator : validators) {
            ValidatorResult<T> result = validator.validate(value);

            if (!result.isValid()) {
                return result;
            }
        }

        return ValidatorResult.valid(value);
    }
}

package ru.tsystems.tsproject.sbb.validation;
import java.util.Map;

public abstract class Validator<T> {

    private T obj;
    private Map<String, String> errors;

    public Validator(T obj) {
        this.obj = obj;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public boolean isValid() {
        errors = validate(obj);
        if (errors.isEmpty())
            return true;
        else return false;
    }

    protected abstract Map<String, String> validate(T obj);
}

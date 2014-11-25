package ru.tsystems.tsproject.sbb.validation.impl;

import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.transferObjects.LoginTO;
import ru.tsystems.tsproject.sbb.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by apple on 25.10.2014.
 */
public class LoginValidatorImpl extends Validator<LoginTO> {
    private static final Pattern loginPattern = Pattern.compile("[\\w]{0,100}", Pattern.UNICODE_CASE);
    private static final Pattern passwordPattern = Pattern.compile("[\\w]{0,100}", Pattern.UNICODE_CASE);

    public LoginValidatorImpl(LoginTO obj) {
        super(obj);
    }

    @Override
    protected Map<String, String> validate(LoginTO loginTO) {
        Map<String, String> errors = new HashMap<String, String>();
        if (loginTO.getLogin() == null || loginTO.getLogin().isEmpty()) {
            errors.put("login", StringHelper.encode("поле не должно быть пустым"));
        } else if (loginTO.getLogin().length() < 5 || loginTO.getLogin().length() > 100) {
            errors.put("login", StringHelper.encode("логин не должен состоять из меньше чем 5 и больше чем 100 символов"));
        } else if (!loginPattern.matcher(loginTO.getLogin()).matches()) {
            errors.put("login", StringHelper.encode("логин должен состоять только из латинских букв и цифр"));
        }
        if (loginTO.getPassword() == null || loginTO.getPassword().isEmpty()) {
            errors.put("password", StringHelper.encode("поле не должно быть пустым"));
        } else if (loginTO.getPassword().length() < 5 || loginTO.getPassword().length() > 100) {
            errors.put("password", StringHelper.encode("пароль не должен состоять из меньше чем 5 и больше чем 100 символов"));
        } else if (!passwordPattern.matcher(loginTO.getPassword()).matches()) {
            errors.put("password", StringHelper.encode("пароль должен состоять только из латинских букв и цифр"));
        }

        return errors;
    }
}

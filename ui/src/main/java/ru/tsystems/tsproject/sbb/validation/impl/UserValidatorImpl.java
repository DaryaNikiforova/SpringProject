package ru.tsystems.tsproject.sbb.validation.impl;

import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;
import ru.tsystems.tsproject.sbb.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Rin on 23.10.2014.
 */
public class UserValidatorImpl extends Validator<UserTO> {
    private static final Pattern namePattern = Pattern.compile("[A-Za-z\\p{L}][A-Za-z-\\p{L}]{0,98}[A-Za-z\\p{L}]", Pattern.UNICODE_CASE);
    private static final Pattern loginPattern = Pattern.compile("[\\w]{1,100}", Pattern.UNICODE_CASE);
    private static final Pattern passwordPattern = Pattern.compile("[\\w]{1,100}", Pattern.UNICODE_CASE);
    private static final Pattern datePattern = Pattern.compile("\\d{2}\\.\\d{2}\\.\\d{4}", Pattern.UNICODE_CASE);

    public UserValidatorImpl(UserTO obj) {
        super(obj);
    }

    @Override
    protected Map<String, String> validate(UserTO user) {
        Map<String, String> errors = new HashMap<String, String>();

        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            errors.put("login", StringHelper.encode("поле не должно быть пустым"));
        } else if (user.getLogin().length() < 5 || user.getLogin().length() > 100) {
            errors.put("login", StringHelper.encode("логин не должен состоять из меньше чем 5 и больше чем 100 символов"));
        } else if(!loginPattern.matcher(user.getLogin()).matches()) {
            errors.put("login", StringHelper.encode("логин должен состоять только из латинских букв и цифр"));
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.put("password", StringHelper.encode("поле не должно быть пустым"));
        } else if (user.getPassword().length() < 5 || user.getPassword().length() > 100) {
            errors.put("password", StringHelper.encode("пароль не должен состоять из меньше чем 5 и больше чем 100 символов"));
        } else if(!passwordPattern.matcher(user.getPassword()).matches()) {
            errors.put("password", StringHelper.encode("пароль должен состоять только из латинских букв и цифр"));
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            errors.put("name", StringHelper.encode("поле не должно быть пустым"));
        } else if (user.getName().length() < 2 || user.getName().length() > 100) {
            errors.put("name", StringHelper.encode("имя не должно состоять из меньше чем 5 и больше чем 100 символов"));
        } else if(!namePattern.matcher(user.getName()).matches()) {
            errors.put("name", StringHelper.encode("имя должно состоять только из букв и знака дефис"));
        }
        if (user.getSurname() == null || user.getSurname().isEmpty()) {
            errors.put("surname", StringHelper.encode("поле не должно быть пустым"));
        } else if (user.getSurname().length() < 2 || user.getSurname().length() > 100) {
            errors.put("surname", StringHelper.encode("фамилия не должна состоять из меньше чем 5 и больше чем 100 символов"));
        } else if(!namePattern.matcher(user.getSurname()).matches()) {
            errors.put("surname", StringHelper.encode("фамилия должна состоять только из букв и знака дефис"));
        }
        if (user.getBirthDate() == null || user.getBirthDate().isEmpty()) {
            errors.put("birth", StringHelper.encode("поле не должно быть пустым"));
        } else if(!datePattern.matcher(user.getBirthDate()).matches()) {
            errors.put("birth", StringHelper.encode("неверный формат даты"));
        } else {
            try {
                Date userDate = new SimpleDateFormat("dd.MM.yyyy").parse(user.getBirthDate());
                Date minDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.1900");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.YEAR, -18);
                Date maxDate = c.getTime();

                if (userDate.before(minDate) || userDate.after(maxDate)) {
                    errors.put("birth", StringHelper.encode("пользователю должно быть не меньше 18 лет"));
                }
            } catch (ParseException ex) {
                errors.put("birth", StringHelper.encode("неверный формат даты"));
            }
        }
        return errors;
    }
}

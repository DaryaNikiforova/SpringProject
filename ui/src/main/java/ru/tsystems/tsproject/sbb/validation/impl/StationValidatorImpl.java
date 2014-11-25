package ru.tsystems.tsproject.sbb.validation.impl;

import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;
import ru.tsystems.tsproject.sbb.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StationValidatorImpl extends Validator<StationTO> {
    private static final Pattern pattern = Pattern.compile("[A-Za-z\\p{L}][A-Za-z- \\p{L}]{0,98}[A-Za-z\\p{L}]", Pattern.UNICODE_CASE);

    public StationValidatorImpl(StationTO obj) {
        super(obj);
    }

    @Override
    protected Map<String, String> validate(StationTO station) {
        Map<String, String> errors = new HashMap<String, String>();
        if (station.getName() == null || station.getName().isEmpty()) {
            errors.put("name", StringHelper.encode("необходимо задать имя"));
        } else if (!pattern.matcher(station.getName()).matches()) {
            errors.put("name", StringHelper.encode(
                    "имя не должно содержать недопустимые символы"));
        } else if (station.getName().length() < 3 || station.getName().length() > 100) {
            errors.put("name", StringHelper.encode(
                    "имя не должно состоять из меньше чем 3 и больше чем 100 символов"));
        }
        return errors;
    }
}

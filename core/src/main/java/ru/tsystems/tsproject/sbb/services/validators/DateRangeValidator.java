package ru.tsystems.tsproject.sbb.services.validators;

import ru.tsystems.tsproject.sbb.services.helpers.TimeHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 16.11.14.
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, String> {

    @Override
    public void initialize(DateRange rangeValid) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd.MM.yyyy");
        String todayString = sdf.format(new Date());
        Date today = null;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(s);
            today = sdf.parse(todayString);
        } catch (ParseException e) {
            e.getStackTrace();
        }
        return !(date.before(today) || date.after(TimeHelper.addDays(today, 45)));
    }
}

package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by apple on 21.11.14.
 */
public class AgeConstraintValidator implements ConstraintValidator<AgeConstraint, String> {
    @Override
    public void initialize(AgeConstraint ageConstraint) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Date birthDate = null;
        Date minDate = null;
        try {
           birthDate = new SimpleDateFormat("dd.MM.yyyy").parse(s);
           minDate = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.1900");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -18);
        Date maxDate = c.getTime();

        return !(birthDate.before(minDate) || birthDate.after(maxDate));
    }
}

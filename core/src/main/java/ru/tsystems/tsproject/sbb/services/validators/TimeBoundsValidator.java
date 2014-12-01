package ru.tsystems.tsproject.sbb.services.validators;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apple on 21.11.14.
 */
public class TimeBoundsValidator implements ConstraintValidator<TimeBounds, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(TimeBounds timeBounds) {
        firstFieldName = timeBounds.min();
        secondFieldName = timeBounds.max();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(o);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        sdf.applyPattern("dd.MM.yyyy HH:mm");
        final Object firstObj = wrapper.getPropertyValue(firstFieldName);
        final Object secondObj = wrapper.getPropertyValue(secondFieldName);
        Date departure = new Date();
        Date arrival = new Date();
        if (firstObj instanceof String && secondObj instanceof String)
        {
            try {
                departure = sdf.parse(firstObj.toString());
                arrival = sdf.parse(secondObj.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return departure.before(arrival);
    }
}

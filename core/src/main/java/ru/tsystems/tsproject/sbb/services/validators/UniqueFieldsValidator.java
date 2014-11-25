package ru.tsystems.tsproject.sbb.services.validators;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by apple on 16.11.14.
 */
public class UniqueFieldsValidator implements ConstraintValidator<UniqueFields, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(UniqueFields uniqueFields) {
        firstFieldName = uniqueFields.first();
        secondFieldName = uniqueFields.second();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        final Object firstObj = wrapper.getPropertyValue(firstFieldName);
        final Object secondObj = wrapper.getPropertyValue(secondFieldName);
        return firstObj == null && secondObj == null || firstObj != null && !firstObj.equals(secondObj);
    }
}

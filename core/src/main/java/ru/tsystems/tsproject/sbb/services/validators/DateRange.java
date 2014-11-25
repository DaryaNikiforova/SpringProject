package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by apple on 16.11.14.
 */
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {
    String message() default "{constraints.RangeValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

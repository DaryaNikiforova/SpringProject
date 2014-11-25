package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by apple on 22.11.14.
 */
@Documented
@Constraint(validatedBy = IncreasingTimeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IncreasingTime {
    String message() default "{constraints.IncreasingTime}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

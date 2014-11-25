package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by apple on 22.11.14.
 */
@Documented
@Constraint(validatedBy = IncreasingDistanceValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IncreasingDistance {
    String message() default "{constraints.IncreasingDistance}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

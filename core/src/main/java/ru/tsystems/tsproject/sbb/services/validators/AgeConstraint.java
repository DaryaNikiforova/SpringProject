package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by apple on 21.11.14.
 */
@Documented
@Constraint(validatedBy = AgeConstraintValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeConstraint {
    String message() default "{constraints.AgeConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

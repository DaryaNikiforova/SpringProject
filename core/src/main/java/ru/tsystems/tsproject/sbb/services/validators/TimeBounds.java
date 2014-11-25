package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by apple on 21.11.14.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeBoundsValidator.class)
@Documented
public @interface TimeBounds {
    String message() default "{constraints.timeBounds}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String min();

    /**
     * @return The second field
     */
    String max();
}

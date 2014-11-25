package ru.tsystems.tsproject.sbb.services.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by apple on 22.11.14.
 */
@Documented
@Constraint(validatedBy = UniqueStationsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStations {
    String message() default "{constraints.UniqueStations}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

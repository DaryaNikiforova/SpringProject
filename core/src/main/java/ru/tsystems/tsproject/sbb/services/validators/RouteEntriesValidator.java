package ru.tsystems.tsproject.sbb.services.validators;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by apple on 22.11.14.
 */
public class RouteEntriesValidator implements ConstraintValidator<RouteEntriesCorrectness, List<RouteEntryTO>>{
    @Override
    public void initialize(RouteEntriesCorrectness routeEntriesCorrectness) {}

    @Override
    public boolean isValid(List<RouteEntryTO> routeEntryTOs, ConstraintValidatorContext constraintValidatorContext) {
        for (RouteEntryTO entry : routeEntryTOs) {
            if (entry.getHour()>=1000 || entry.getMinute()>=60 || entry.getHour() < 0 || entry.getMinute() < 0)
                return false;
        }
        return true;
    }
}

package ru.tsystems.tsproject.sbb.services.validators;

import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by apple on 22.11.14.
 */
public class IncreasingTimeValidator implements ConstraintValidator<IncreasingTime, List<RouteEntryTO>> {
    @Override
    public void initialize(IncreasingTime increasingTime) {}

    @Override
    public boolean isValid(List<RouteEntryTO> routeEntryTOs, ConstraintValidatorContext constraintValidatorContext) {
        int prevHour = 0;
        int prevMinute = 0;
        for (int i = 1; i < routeEntryTOs.size(); i++) {
            RouteEntryTO entry = routeEntryTOs.get(i);
            if (entry.getHour() < prevHour || (entry.getHour() == prevHour && entry.getMinute() <= prevMinute)) {
                return false;
            }
            prevHour = entry.getHour();
            prevMinute = entry.getMinute();
        }
        return true;
    }
}

package ru.tsystems.tsproject.sbb.services.validators;

import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by apple on 22.11.14.
 */
public class IncreasingDistanceValidator implements ConstraintValidator<IncreasingDistance, List<RouteEntryTO>> {
    @Override
    public void initialize(IncreasingDistance increasingDistance) {}

    @Override
    public boolean isValid(List<RouteEntryTO> routeEntryTOs, ConstraintValidatorContext constraintValidatorContext) {
        int prevDistance = 0;
        for (int i = 1; i < routeEntryTOs.size(); i++) {
            RouteEntryTO entry = routeEntryTOs.get(i);
            if (entry.getDistance() <= prevDistance) {
                return false;
            }
            prevDistance = entry.getDistance();
        }
        return true;
    }
}

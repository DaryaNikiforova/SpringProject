package ru.tsystems.tsproject.sbb.services.validators;

import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 22.11.14.
 */
public class UniqueStationsValidator implements ConstraintValidator<UniqueStations, List<RouteEntryTO>> {
    @Override
    public void initialize(UniqueStations uniqueStations) {}

    @Override
    public boolean isValid(List<RouteEntryTO> routeEntries, ConstraintValidatorContext constraintValidatorContext) {
        List<String> stations = new ArrayList<String>();
        for (RouteEntryTO entry : routeEntries) {
            if (stations.contains(entry.getStationName())) {
                return false;
            }
            stations.add(entry.getStationName());
        }
        return true;
    }

}

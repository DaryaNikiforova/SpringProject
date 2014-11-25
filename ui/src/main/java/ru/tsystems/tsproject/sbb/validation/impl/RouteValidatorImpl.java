package ru.tsystems.tsproject.sbb.validation.impl;

import ru.tsystems.tsproject.sbb.helpers.StringHelper;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;
import ru.tsystems.tsproject.sbb.validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 23.10.2014.
 */
public class RouteValidatorImpl extends Validator<RouteTO> {

    public RouteValidatorImpl(RouteTO obj) {
        super(obj);
    }

    @Override
    protected Map<String, String> validate(RouteTO route) {
        Map<String, String> errors = new HashMap<String, String>();

        int prevHour = 0;
        int prevMinute = 0;
        int prevDistance = 0;

        List<String> stations = new ArrayList<String>();
        stations.add(route.getRouteEntries().get(0).getStationName());

        for (int i = 1; i < route.getRouteEntries().size(); i++) {
            RouteEntryTO entry = route.getRouteEntries().get(i);
            if (entry.getHour() <= prevHour ||
                    (entry.getHour() <= prevHour && entry.getMinute() <= prevMinute)) {
                errors.put("hour"+ (i + 1), StringHelper.encode(
                        "Время в пути для станции№" + (i + 1) + " должно быть больше предыдущей"));
            }
            if (entry.getHour() >= 1000) {
                errors.put("hour"+ (i + 1), StringHelper.encode(
                        "Число часов в пути для станции№" + (i + 1) + " слишком большое"));
            }
            if (entry.getMinute() >= 60) {
                errors.put("minute"+ (i + 1), StringHelper.encode(
                        "Число минут в пути для станции№" + (i + 1) + " неверное"));
            }
            if (entry.getDistance() <= prevDistance) {
                errors.put("distance"+ (i + 1), StringHelper.encode(
                        "Дистанция для станции№" + (i + 1) + " должна быть больше предыдущей"));
            }
            if (stations.contains(entry.getStationName())) {
                errors.put("name"+ (i + 1), StringHelper.encode(
                        "Станции не должны повторятся"));
            }
            stations.add(entry.getStationName());
        }
        return errors;
    }
}

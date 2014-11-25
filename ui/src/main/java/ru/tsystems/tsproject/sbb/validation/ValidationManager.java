package ru.tsystems.tsproject.sbb.validation;

import ru.tsystems.tsproject.sbb.transferObjects.*;
import ru.tsystems.tsproject.sbb.validation.impl.*;

public class ValidationManager {
    public static Validator getValidator(Object obj) {
        if (StationTO.class.equals(obj.getClass())) {
            return new StationValidatorImpl((StationTO) obj);
        }
        if (TrainTO.class.equals(obj.getClass())) {
            return new TrainValidatorImpl((TrainTO) obj);
        }
        if (RouteTO.class.equals(obj.getClass())) {
            return new RouteValidatorImpl((RouteTO) obj);
        }
        if (UserTO.class.equals(obj.getClass())) {
            return new UserValidatorImpl((UserTO) obj);
        }
        if (LoginTO.class.equals(obj.getClass())) {
            return new LoginValidatorImpl((LoginTO) obj);
        }
        if (SearchTO.class.equals(obj.getClass())) {
            return new SearchValidatorImpl((SearchTO) obj);
        }
        if (SearchStationTO.class.equals(obj.getClass())) {
            return new SearchStationValidatorImpl((SearchStationTO) obj);
        }
        else return null;
    }
}

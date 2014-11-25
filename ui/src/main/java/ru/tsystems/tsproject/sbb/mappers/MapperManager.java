package ru.tsystems.tsproject.sbb.mappers;

import ru.tsystems.tsproject.sbb.mappers.impl.*;
import ru.tsystems.tsproject.sbb.transferObjects.*;

/**
 * Created by apple on 22.10.2014.
 */
public class MapperManager {
    public static Mapper getMapper(Class TOType) {
        if (StationTO.class.equals(TOType)) {
            return new StationMapperImpl();
        }
        if (TrainTO.class.equals(TOType)) {
            return new TrainMapperImpl();
        }
        if (RouteTO.class.equals(TOType)) {
            return new RouteMapperImpl();
        }
        if (UserTO.class.equals(TOType)) {
            return new UserMapperImpl();
        }
        if (LoginTO.class.equals(TOType)) {
            return new LoginMapperImpl();
        }
        if (SearchTO.class.equals(TOType)) {
            return new SearchMapperImpl();
        }
        if (SearchStationTO.class.equals(TOType)) {
            return new SearchStationMapperImpl();
        }
        return null;
    }
}

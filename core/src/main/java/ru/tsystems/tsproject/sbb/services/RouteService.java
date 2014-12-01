package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;
import ru.tsystems.tsproject.sbb.database.entity.Route;
import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;
import ru.tsystems.tsproject.sbb.database.entity.Station;
import ru.tsystems.tsproject.sbb.database.repositories.RouteEntryRepository;
import ru.tsystems.tsproject.sbb.database.repositories.RouteRepository;
import ru.tsystems.tsproject.sbb.database.repositories.StationRepository;
import ru.tsystems.tsproject.sbb.services.exceptions.RouteNotFoundException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.services.helpers.RouteHelper;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Implements methods interacting with DAO methods. Needs to get and change data in database.
 * @author Daria Nikiforova
 */

@Service
@Transactional
@PreAuthorize("denyAll")
public class RouteService {
    /**
     * Creating factory to get needed DAO instance.
     */
    private static final Logger LOGGER = Logger.getLogger(Station.class);

    @Inject RouteRepository routeRepository;
    @Inject RouteEntryRepository routeEntryRepository;
    @Inject StationRepository stationRepository;


    /**
     * Returns all trains routes from database.
     * @return list of trains routes.
     */
    @PreAuthorize("hasRole('admin')")
    public List<RouteTO> getAllRoutes() {
        LOGGER.info("Получение списка всех маршрутов из базы данных");
        List<Route> routes = routeRepository.findAll();
        List<RouteTO> results = new ArrayList<RouteTO>();
        for (Route r:routes) {
            RouteTO route = new RouteTO();
            route.setNumber(r.getId());
            route.setDistance(RouteHelper.getRouteDistance(r));
            route.setRoute(RouteHelper.getRouteName(r));
            route.setTime(RouteHelper.getRouteTime(r));
            results.add(route);
        }
        LOGGER.info("Найдено " + results.size() + " маршрутов");
        return results;
    }


    /**
     * Adds route information to database. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @param routeTO route that need to add.
     * @throws StationNotFoundException
     */

    @PreAuthorize("hasRole('admin')")
    public void addRoute(RouteTO routeTO) {
            Route route = new Route();
            route.setRouteEntries(mapRouteEntries(routeTO.getRouteEntries(), route));
            routeRepository.save(route);
            LOGGER.info("Добавление в таблицу маршрута");
    }

    private List<RouteEntry> mapRouteEntries(List<RouteEntryTO> routeEntryTOs, Route route) {
        List<RouteEntry> routeEntries = new ArrayList<RouteEntry>();
        for (int i = 0; i < routeEntryTOs.size(); i++) {
            RouteEntryTO routeEntryTO = routeEntryTOs.get(i);
            RouteEntry routeEntry = new RouteEntry();
            routeEntry.setDistance(routeEntryTO.getDistance());
            routeEntry.setMinute(routeEntryTO.getMinute());
            routeEntry.setHour(routeEntryTO.getHour());
            routeEntry.setSeqNumber(i+1);
            routeEntry.setStation(stationRepository.findByName(routeEntryTO.getStationName()));
            routeEntry.setRoute(route);
            routeEntries.add(routeEntry);
        }
        return routeEntries;
    }

    @PreAuthorize("hasRole('admin')")
    public List<RouteTO> getRoutesByStation(int id) throws StationNotFoundException {
        LOGGER.info("Получение списка маршрутов для станции с id=" + id);
        Station station = stationRepository.findOne(id);
        if (station == null) {
            LOGGER.error("Станции с id=" + id + " в базе данных не существует");
            throw new StationNotFoundException("Станции с id=" + id + " в базе данных не существует");
        }
        List<RouteEntry> list = routeEntryRepository.findByStation_Id(station.getId());
        List<RouteTO> results = new ArrayList<RouteTO>();
        for (RouteEntry routeEntry : list) {
            RouteTO routeTO = new RouteTO();
            routeTO.setNumber(routeEntry.getRoute().getId());
            if(!results.contains(routeTO)) {
                results.add(routeTO);
            }
        }
        return results;
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteRoute(RouteTO route) throws RouteNotFoundException {
        if (routeRepository.findOne(route.getNumber()) == null) {
            throw new RouteNotFoundException("Маршрута с id=" + route.getNumber() +" не существует в базе данных");
        }
        List<RouteEntry> entries = routeEntryRepository.findByRoute_Id(route.getNumber());
        for (RouteEntry entry : entries) {
            routeEntryRepository.delete(entry);
        }
        routeRepository.delete(route.getNumber());
        LOGGER.info("Удаление маршрута " + route.getRoute() + " из базы данных");
    }

    @PreAuthorize("hasRole('admin')")
    public RouteTO getRoute(int id) throws RouteNotFoundException {
        Route route = routeRepository.findOne(id);
        if (route == null) {
            throw new RouteNotFoundException("Маршрута с id=" + id +" не существует в базе данных");
        }
        RouteTO routeTO = new RouteTO();
        routeTO.setNumber(route.getId());
        AutoPopulatingList<RouteEntryTO> routeEntryTOs = new AutoPopulatingList<RouteEntryTO>(RouteEntryTO.class);
        List<RouteEntry> routeEntries = route.getRouteEntries();
        Collections.sort(routeEntries, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });
        for (RouteEntry entry : routeEntries) {
            RouteEntryTO entryTO = new RouteEntryTO();
            entryTO.setStationName(entry.getStation().getName());
            entryTO.setSeqNumber(entry.getSeqNumber());
            entryTO.setDistance((int) entry.getDistance());
            entryTO.setHour(entry.getHour());
            entryTO.setMinute(entry.getMinute());
            routeEntryTOs.add(entryTO);
        }
        routeTO.setRouteEntries(routeEntryTOs);
        return routeTO;
    }

    @PreAuthorize("hasRole('admin')")
    public void editRoute(RouteTO routeTO) throws RouteNotFoundException {
        Route route = routeRepository.findOne(routeTO.getNumber());
        if (route == null) {
            throw new RouteNotFoundException("Маршрута с id=" + routeTO.getNumber() +" не существует в базе данных");
        }
        List<RouteEntry> routeEntries = route.getRouteEntries();
        Collections.sort(routeEntries, new Comparator<RouteEntry>() {
            public int compare(RouteEntry o1, RouteEntry o2) {
                return o1.getSeqNumber() - o2.getSeqNumber();
            }
        });
        List<RouteEntry> newRouteEntries = mapRouteEntries(routeTO.getRouteEntries(), route);
        for (int i = 0; i < newRouteEntries.size(); i++) {
            if (i < routeEntries.size())
                newRouteEntries.get(i).setId(routeEntries.get(i).getId());
        }
        for (RouteEntry routeEntry : newRouteEntries) {
            routeEntryRepository.save(routeEntry);
        }
        if (newRouteEntries.size() < routeEntries.size()) {
            for (int i = newRouteEntries.size(); i < routeEntries.size(); i++) {
                routeEntryRepository.delete(routeEntries.get(i));
            }
        }

        route.setRouteEntries(newRouteEntries);
        routeRepository.save(route);
        LOGGER.info("Изменение маршрута №" + routeTO.getNumber() + " в таблице");
    }
}

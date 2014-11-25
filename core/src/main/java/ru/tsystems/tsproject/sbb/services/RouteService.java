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
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.services.helpers.RouteHelper;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
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
        return results;
    }


    /**
     * Adds route information to database. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @param routeTO route that need to add.
     * @throws StationNotFoundException
     */

    @PreAuthorize("hasRole('admin')")
    public void addRoute(RouteTO routeTO) throws StationNotFoundException {
            Route route = new Route();
            route.setRouteEntries(mapRouteEntries(routeTO.getRouteEntries(), route));
            routeRepository.save(route);
            LOGGER.info("Добавление в таблицу маршрута");
    }

    List<RouteEntry> mapRouteEntries(List<RouteEntryTO> routeEntryTOs, Route route) throws StationNotFoundException {
        List<RouteEntry> routeEntries = new ArrayList<RouteEntry>();

        for (int i = 0; i < routeEntryTOs.size(); i++) {
            RouteEntryTO routeEntryTO = routeEntryTOs.get(i);
            RouteEntry routeEntry = new RouteEntry();
            routeEntry.setDistance(routeEntryTO.getDistance());
            routeEntry.setMinute(routeEntryTO.getMinute());
            routeEntry.setHour(routeEntryTO.getHour());
            routeEntry.setSeqNumber(i+1);
            try {
                routeEntry.setStation(stationRepository.findByName(routeEntryTO.getStationName()));
            } catch (PersistenceException ex) {
                LOGGER.error("Ошибка при получении информации о станции");
                throw new StationNotFoundException("Ошибка при получении информации о станции");
            }
            routeEntry.setRoute(route);
            routeEntries.add(routeEntry);
        }
        return routeEntries;
    }

    @PreAuthorize("hasRole('admin')")
    public List<RouteTO> getRoutesByStation(int id) throws ServiceException {
        List<RouteEntry> list = new ArrayList<RouteEntry>();
        try {
            list = routeEntryRepository.findByStation_Id(id);
        } catch (PersistenceException ex) {
            LOGGER.error("Невозможно получить информацию о маршрутах");
            throw new ServiceException("Невозможно получить информацию о маршрутах");
        }
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
    public void deleteRoute(RouteTO route) throws Exception {
        if (routeRepository.findOne(route.getNumber()) == null) {
            throw new Exception();
        }
        List<RouteEntry> entries = routeEntryRepository.findByRoute_Id(route.getNumber());
        for (RouteEntry entry : entries) {
            routeEntryRepository.delete(entry);
        }
        routeRepository.delete(route.getNumber());
        LOGGER.info("Удаление маршрута " + route.getRoute() + " из базы данных");
    }

    @PreAuthorize("hasRole('admin')")
    public RouteTO getRoute(int id) {
        Route route = routeRepository.findOne(id);
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
    public void editRoute(RouteTO routeTO) throws Exception {
        Route route = routeRepository.findOne(routeTO.getNumber());
        if (route == null) {
            throw new Exception();
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

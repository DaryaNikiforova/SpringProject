package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;
import ru.tsystems.tsproject.sbb.database.entity.Trip;
import ru.tsystems.tsproject.sbb.database.repositories.RouteEntryRepository;
import ru.tsystems.tsproject.sbb.database.repositories.TripRepository;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.helpers.RouteHelper;
import ru.tsystems.tsproject.sbb.services.helpers.TimeHelper;
import ru.tsystems.tsproject.sbb.transferObjects.TimetableTO;
import ru.tsystems.tsproject.sbb.transferObjects.TripTO;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implements methods interacting with DAO methods. Needs to get and change data in database.
 * @author Daria Nikiforova
 */

@Service
@Transactional
@PreAuthorize("denyAll")
public class TripService {
    private static final Logger LOGGER = Logger.getLogger(TripService.class);

    @Inject RouteEntryRepository routeEntryRepository;
    @Inject TripRepository tripRepository;
    @Inject TicketService ticketService;
    /**
     * Returns timetable records routes contains specified dates in specified time intervals.
     * Throws exception if database connection is lost, bad request or error with transaction.
     * @param stationFrom begin of route.
     * @param stationTo end of route.
     * @param timeFrom interval begin.
     * @param timeTo interval end.
     * @return list of timetable records.
     * @throws ServiceException
     */

    @PreAuthorize("permitAll")
    public List<TimetableTO> searchTrains(String stationFrom, String stationTo, Date timeFrom, Date timeTo) throws ServiceException {
        List<TimetableTO> results = new ArrayList<TimetableTO>();
        List<RouteEntry> list = new ArrayList<RouteEntry>();
        try {
            list = routeEntryRepository.findByStations(stationFrom, stationTo);
        }
        catch (PersistenceException ex) {
            LOGGER.error("Невозможно получить информацию о маршрутах");
            throw new ServiceException("Невозможно получить информацию о маршрутах");
        }

            for (int i = 0; i < list.size(); i++) {
                int hour = list.get(i).getHour();
                int minute = list.get(i).getMinute();
                Date depTimeFrom = TimeHelper.getHourBack(timeFrom, hour, minute);
                Date depTimeTo = TimeHelper.getHourBack(timeTo, hour, minute);
                try {
                    List<Trip> trips = tripRepository.findByRoute_IdAndDepartureTimeBetween(list.get(i).getRoute().getId(), depTimeFrom, depTimeTo);
                    if (trips != null) {
                        for (Trip t : trips) {
                            TimetableTO result = new TimetableTO();
                            RouteEntry arrivalEntry = routeEntryRepository.findByStation_NameAndRoute_Id(stationTo, list.get(i).getRoute().getId());
                            Date depDate = TimeHelper.addHours(t.getDepartureTime(), hour, minute);
                            Date arriveDate = TimeHelper.addHours(t.getDepartureTime(), arrivalEntry.getHour(), arrivalEntry.getMinute());
                            result.setTripId(t.getId());
                            result.setRouteName(RouteHelper.getRouteName(t.getRoute()));
                            result.setTrainName(t.getTrain().getName());
                            result.setDepDate(depDate);
                            result.setStationFrom(stationFrom);
                            result.setArriveDate(arriveDate);
                            result.setStationTo(stationTo);
                            result.setTime(RouteHelper.getRouteTime(t.getRoute(), stationFrom, stationTo));
                            result.setDistance(RouteHelper.getRouteDistance(t.getRoute()));
                            result.setTrainNumber(t.getTrain().getId());
                            result.setSeatCount(ticketService.getAvaliableSeats(stationFrom, stationTo, t.getId(), t.getRoute().getId()).size());
                            results.add(result);
                        }
                    }
                } catch (PersistenceException ex) {
                    LOGGER.error("Невозможно получить информацию о расписании");
                    throw new ServiceException("Невозможно получить информацию о расписании");
                }
            }
        return results;
    }

    /**
     * Returns timetable list for routes that includes specified station. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @param name station name.
     * @param depDate date of departure.
     * @return timetable list.
     * @throws ServiceException
     */

    @PreAuthorize("permitAll")
    public List<TimetableTO> getRoutesByStation(String name, Date depDate) throws ServiceException {
        List<RouteEntry> list = new ArrayList<RouteEntry>();
        try {
            list = routeEntryRepository.findByStation_Name(name);
        } catch (PersistenceException ex) {
            LOGGER.error("Невозможно получить информацию о маршрутах");
            throw new ServiceException("Невозможно получить информацию о маршрутах");
        }
        List<TimetableTO> results = new ArrayList<TimetableTO>();
        for (int i=0; i<list.size(); i++) {
            int hour = list.get(i).getHour();
            int minute = list.get(i).getMinute();
            Date depTime = TimeHelper.getHourBack(depDate, hour, minute);
            try {
                List<Trip> trips = tripRepository.findByRoute_IdAndDepartureTimeBetween
                        (list.get(i).getRoute().getId(), depTime, TimeHelper.addHours(depDate, 23, 59));
                if (trips != null) {
                    for (int k = 0; k < trips.size(); k++) {
                        RouteEntry re = routeEntryRepository.findLastByRoute_Id(trips.get(k).getRoute().getId());
                        if (!name.equals(re.getStation().getName())) {
                            TimetableTO result = new TimetableTO();
                            result.setRouteName(RouteHelper.getRouteName(trips.get(k).getRoute()));
                            result.setTrainName(trips.get(k).getTrain().getName());
                            result.setDepDate(TimeHelper.addHours(trips.get(k).getDepartureTime(), hour, minute));
                            result.setStationFrom(name);
                            result.setArriveDate(TimeHelper.addHours(trips.get(k).getDepartureTime(), re.getHour(), re.getMinute()));
                            result.setStationTo(re.getStation().getName());
                            result.setTrainNumber(trips.get(k).getTrain().getId());
                            results.add(result);
                        }
                    }
                }
            } catch (PersistenceException ex) {
                LOGGER.error("Невозможно получить информацию о расписании");
                throw new ServiceException("Невозможно получить информацию о расписании");
            }

        }
        return results;
    }

    /**
     * Returns trips list for routes that contains in database. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @return list of trips
     * @throws ServiceException
     */

    @PreAuthorize("hasRole('admin')")
    public List<TripTO> getAllTrips() throws ServiceException {
        List<Trip> trips = new ArrayList<Trip>();
        try {
            trips = tripRepository.findAll();
        } catch (PersistenceException ex) {
            LOGGER.error("Невозможно получить информацию о рейсах");
            throw new ServiceException("Невозможно получить информацию о рейсах");
        }
        List<TripTO> result = new ArrayList<TripTO>();
        for (Trip t : trips) {
            TripTO trip = new TripTO();
            trip.setArrival(RouteHelper.getArrivalDate(t.getRoute(), t.getDepartureTime()));
            trip.setDeparture(t.getDepartureTime());
            trip.setNumber(t.getTrain().getId());
            trip.setSeatCount(t.getTrain().getSeatCount());
            String trainName = t.getTrain().getName();
            if (trainName != null) {
                trip.setRoute(RouteHelper.getRouteName(t.getRoute()) + " &laquo;" + trainName + "&raquo;");
            } else {
                trip.setRoute(RouteHelper.getRouteName(t.getRoute()));
            }
            trip.setId(t.getId());
            result.add(trip);
        }
        return result;
    }

    @PreAuthorize("hasRole('admin')")
    public List<TripTO> getTripsByTrain(int id) {
        List<Trip> trips = tripRepository.findByTrain_Id(id);
        List<TripTO> results = new ArrayList<TripTO>();
        for (Trip trip : trips) {
            TripTO t = new TripTO();
            t.setId(trip.getId());
            results.add(t);
        }
        return results;
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteTrip(TripTO trip) throws Exception {
        if (tripRepository.findOne(trip.getId()) == null) {
            throw new Exception();
        }
        tripRepository.delete(trip.getId());
        LOGGER.info("Удаление рейса " + trip.getRoute() + " из базы данных");
    }

    @PreAuthorize("hasRole('admin')")
    public List<TripTO> getTripsByRoute(int id) {
        List<Trip> trips = tripRepository.findByRoute_Id(id);
        List<TripTO> results = new ArrayList<TripTO>();
        for (Trip t : trips) {
            TripTO trip = new TripTO();
            trip.setId(t.getId());
            results.add(trip);
        }
        return results;
    }
}

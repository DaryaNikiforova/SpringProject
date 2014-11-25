package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.*;
import ru.tsystems.tsproject.sbb.database.repositories.*;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.TimeConstraintException;
import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyRegisteredException;
import ru.tsystems.tsproject.sbb.services.helpers.TicketHelper;
import ru.tsystems.tsproject.sbb.services.helpers.TimeHelper;
import ru.tsystems.tsproject.sbb.transferObjects.PassengerTO;
import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implements methods interacting with DAO methods. Needs to get and change data in database.
 * @author Daria Nikiforova
 */

@org.springframework.stereotype.Service
@Transactional
@PreAuthorize("denyAll")
public class TicketService {

    private static final Logger LOGGER = Logger.getLogger(TicketService.class);

    @Inject StationRepository stationRepository;
    @Inject UserRepository userRepository;
    @Inject TripRepository tripRepository;
    @Inject TicketRepository ticketRepository;
    @Inject RouteEntryRepository routeEntryRepository;
    @Inject ServiceRepository serviceRepository;
    @Inject RateRepository rateRepository;

    /**
     * Adds ticket to database.Throws exception if database connection is lost,
     * bad request or error with transaction. Throws UserAlreadyRegisterException when user
     * trying to buy ticket on trip not at first time. Throws TimeConstraintException when trying
     * to buy ticket less than 10 minutes before the departure
     * @param ticket contains information that will be added
     * @throws ServiceException
     */

    @PreAuthorize("isAuthenticated()")
    public void AddTicket(TicketTO ticket) throws ServiceException {

        Date purchaseDate = new Date();
        double minutes = (ticket.getDeparture().getTime() - purchaseDate.getTime()) / (60 * 1000);
        try {
            if (minutes >= 10) {
                if (ticketRepository.countByUser_NameAndUser_SurnameAndUser_BirthDateAndTrip_Id(ticket.getUserName(), ticket.getUserSurname(),
                        new SimpleDateFormat("dd.MM.yyyy").parse(ticket.getBirthDate()), ticket.getTripId())==0
                    && ticket.getSeatNumber() > 0) {
                    Ticket t = new Ticket();
                    t.setPrice(ticket.getPrice());
                    t.setSeat(ticket.getSeatNumber());
                    Station s = new Station();
                    s.setName(ticket.getStationFrom());
                    s.setId(stationRepository.findByName(ticket.getStationFrom()).getId());
                    t.setStationFrom(s);
                    s = new Station();
                    s.setName(ticket.getStationTo());
                    s.setId(stationRepository.findByName(ticket.getStationTo()).getId());
                    t.setStationTo(s);
                    t.setUser(userRepository.findByLogin(ticket.getLogin()));
                    t.setTrip(tripRepository.findOne(ticket.getTripId()));
                    t.setDate(purchaseDate);
                    t.setRate(new Rate(ticket.getRateType()));
                    if (ticket.getServices() != null) {
                        for (String k : ticket.getServices()) {
                        Service service = serviceRepository.findByName(k);
                        t.getServices().add(service);
                       }
                    }
                    ticketRepository.save(t);
                    LOGGER.info("Добавление билета в базу данных");
                }
                else {
                    LOGGER.error("Вы уже зарегистрированы на этот рейс");
                    throw new UserAlreadyRegisteredException("Вы уже зарегистрированы на этот рейс");
                }
            }
            else {
                LOGGER.error("Вы не можете купить билет меньше, чем за 10 минут до отправления поезда");
                throw new TimeConstraintException("Вы не можете купить билет меньше, чем за 10 минут до отправления поезда");
            }
        }  catch (PersistenceException e) {
            LOGGER.error("Произошла ошибка при добавлении билета");
            throw new ServiceException("Произошла ошибка при добавлении билета");
         } catch (ParseException e) {
            LOGGER.error("Некорректный формат даты");
            throw new ServiceException("Некорректный формат даты");
        }
    }
    /**
     * Returns ticket information that needed for client. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @param tripId of specified trip.
     * @param stationFrom which begins route for passenger.
     * @param stationTo which ends route for passenger.
     * @param login users login.
     * @return TicketTO entity for client.
     * @throws ServiceException
     */

    @PreAuthorize("isAuthenticated()")
    public TicketTO generateTicket(int tripId, String stationFrom, String stationTo, String login) throws ServiceException{
        TicketTO ticket = new TicketTO();
        try {
            LOGGER.info("Оформление билета на рейс");
            Trip trip = tripRepository.findOne(tripId);
            ticket.setStationFrom(stationFrom);
            ticket.setStationTo(stationTo);
            ticket.setRoute(stationFrom + "&nbsp;→&nbsp;" + stationTo);
            ticket.setTrip(TicketHelper.getTrainInfo(trip));
            ticket.setTripId(tripId);
            RouteEntry reFrom = routeEntryRepository.findByStation_NameAndRoute_Id(stationFrom, trip.getRoute().getId());
            RouteEntry reTo = routeEntryRepository.findByStation_NameAndRoute_Id(stationTo, trip.getRoute().getId());
            Date depDate = TimeHelper.addHours(trip.getDepartureTime(), reFrom.getHour(), reFrom.getMinute());
            Date arrDate = TimeHelper.addHours(trip.getDepartureTime(), reTo.getHour(), reTo.getMinute());
            ticket.setDeparture(depDate);
            ticket.setArrival(arrDate);
            User user = userRepository.findByLogin(login);
            ticket.setUserName(user.getName());
            ticket.setLogin(login);
            ticket.setUserSurname(user.getSurname());
            ticket.setBirthDate(TimeHelper.getDatePart(user.getBirthDate()));
            ticket.setSeats(getAvaliableSeats(stationFrom, stationTo, tripId, trip.getRoute().getId()));
            ticket.setTrainRate(trip.getTrain().getRate().getId());
            ticket.setRouteId(trip.getRoute().getId());
            List<String> services = new ArrayList<String>();
            List<Service> serviceList = serviceRepository.findAll();
            for (Service s : serviceList) {
                services.add(s.getName());
            }
            ticket.setServices(services);
            Map<Long, String> rates = new HashMap<Long, String>();
            List<Rate> rateList = rateRepository.findByForTrainFalse();
            for (Rate r : rateList) {
                rates.put((long) r.getId(), r.getName());
            }

            ticket.setRateTypes(rates);
        } catch (PersistenceException ex) {
            LOGGER.error("Произошла ошибка при оформлении билета");
            throw new ServiceException("Произошла ошибка при оформлении билета");
        }
        return ticket;
    }

    /**
     * Calculates ticket price. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @param ticket that will bought.
     * @return price value
     * @throws ServiceException
     */

    @PreAuthorize("isAuthenticated()")
    public double calcPrice(TicketTO ticket) throws ServiceException {
        double price = 0;
        try {
            RouteEntry reFrom = routeEntryRepository.findByStation_NameAndRoute_Id(ticket.getStationFrom(), ticket.getRouteId());
            RouteEntry reTo = routeEntryRepository.findByStation_NameAndRoute_Id(ticket.getStationTo(), ticket.getRouteId());
            double distance = reTo.getDistance() - reFrom.getDistance();

            if (ticket.getServices().size() > 0) {
                List<String> services = ticket.getServices();
                for (String k : services)
                    price += serviceRepository.findByName(k).getValue();
            }
            price += distance * rateRepository.findOne(ticket.getRateType()).getValue() *
                     rateRepository.findOne(ticket.getTrainRate()).getValue();
        } catch (PersistenceException ex) {
            LOGGER.error("Ошибка при вычислении стоимости билета");
            throw new ServiceException("Ошибка при вычислении стоимости билета");
        }
        return price;
    }

    /**
     * Returns list of seat number that not bought.
     * @param stationFrom route begin.
     * @param stationTo route end.
     * @param tripId id of specified trip.
     * @param routeId id of specified route.
     * @return list of available seat numbers.
     */

    @PreAuthorize("isAuthenticated()")
    public List<Integer> getAvaliableSeats (String stationFrom, String stationTo, int tripId, int routeId) {
            List<Ticket> tickets = ticketRepository.findByTrip_Id(tripId);
            Trip trip = tripRepository.findOne(tripId);
            Map<String, Integer> stations = new HashMap<String, Integer>();
            List<RouteEntry> entries = routeEntryRepository.findByRoute_Id(routeId);
        for (RouteEntry r : entries) {
            stations.put(r.getStation().getName(), r.getSeqNumber());
        }
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i <= trip.getTrain().getSeatCount(); i++) {
            boolean isReserved = false;
            for (Ticket t : tickets) {
                if (t.getSeat() == i && stations.get(stationFrom) < stations.get(t.getStationTo().getName())
                        || stations.get(stationTo) < stations.get(t.getStationFrom().getName())) {
                    isReserved = true;
                    break;
                }
            }
            if (!isReserved) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Returns list of passengers on specified trip.
     * @param tripId id of specified trip.
     * @return list of passengers which buy ticket on trip.
     */

    @PreAuthorize("hasRole('admin')")
    public List<PassengerTO> getPassengersByTrip(int tripId) {
        List<Ticket> tickets = ticketRepository.findByTrip_Id(tripId);
        List<PassengerTO> passengers = new ArrayList<PassengerTO>();
        for (Ticket t : tickets) {
            PassengerTO p = new PassengerTO();
            p.setName(t.getUser().getName());
            p.setSurname(t.getUser().getSurname());
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            p.setBirthDate(df.format(t.getUser().getBirthDate()));
            p.setSeat(t.getSeat());
            p.setPassRoute(t.getStationFrom().getName() + "&nbsp;→&nbsp;" + t.getStationTo().getName());
            passengers.add(p);
        }
        return passengers;
    }

}

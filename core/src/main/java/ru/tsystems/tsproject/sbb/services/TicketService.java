package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.*;
import ru.tsystems.tsproject.sbb.database.repositories.*;
import ru.tsystems.tsproject.sbb.services.exceptions.*;
import ru.tsystems.tsproject.sbb.services.helpers.TicketHelper;
import ru.tsystems.tsproject.sbb.services.helpers.TimeHelper;
import ru.tsystems.tsproject.sbb.transferObjects.PassengerTO;
import ru.tsystems.tsproject.sbb.transferObjects.ServiceTO;
import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;

import javax.inject.Inject;
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
    @Inject RouteRepository routeRepository;

    /**
     * Adds ticket to database.Throws exception if database connection is lost,
     * bad request or error with transaction. Throws UserAlreadyRegisterException when user
     * trying to buy ticket on trip not at first time. Throws TimeConstraintException when trying
     * to buy ticket less than 10 minutes before the departure
     * @param ticket contains information that will be added
     * @throws ServiceException
     */

    @PreAuthorize("isAuthenticated()")
    public void AddTicket(TicketTO ticket) throws SeatAlreadyRegisteredException, UserAlreadyRegisteredException,
                                                  TimeConstraintException {

        Date purchaseDate = new Date();
        if (ticketRepository.countByTripIdAndSeat(ticket.getTripId(), ticket.getSeatNumber()) > 0) {
            throw new SeatAlreadyRegisteredException("Билет на место с номером " + ticket.getSeatNumber() + " уже куплен");
        }
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
                        for (ServiceTO k : ticket.getServices()) {
                        Service service = serviceRepository.findOne(k.getId());
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
         } catch (ParseException e) {
            LOGGER.error("Некорректный формат даты");
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
    public TicketTO generateTicket(int tripId, String stationFrom, String stationTo, String login) throws TripNotFoundException,
            TripDetailsNotFoundException, UserNotFoundException, UserAlreadyRegisteredException, TimeConstraintException, NoAvailableSeats {
        TicketTO ticket = new TicketTO();
        LOGGER.info("Оформление билета на рейс с id=" + tripId);
        Trip trip = tripRepository.findOne(tripId);
        if (trip == null) {
            LOGGER.error("Рейса с id=" + tripId + " не существует в базе данных");
            throw new TripNotFoundException("Рейса с id=" + tripId + " не существует в базе данных");
        }
        Date purchaseDate = new Date();
        try {
            RouteEntry reFrom=routeEntryRepository.findByStation_NameAndRoute_Id(stationFrom,trip.getRoute().getId());
            RouteEntry reTo=routeEntryRepository.findByStation_NameAndRoute_Id(stationTo,trip.getRoute().getId());
            if(reFrom==null || reTo==null){
                LOGGER.error("Информация о маршруте рейса " + tripId + "не найдена в базе данных");
                throw new TripDetailsNotFoundException("Информация о маршруте рейса " + tripId + "не найдена в базе данных");
            }
            List<Integer> tickets = getAvaliableSeats(stationFrom, stationTo, trip.getId());
            if (tickets.isEmpty()) {
                LOGGER.error("На поезд с номером" + trip.getTrain().getId() + " нет свободных мест");
                throw new NoAvailableSeats("На поезд с номером" + trip.getTrain().getId() + " нет свободных мест");
            }
            ticket.setStationFrom(stationFrom);
            ticket.setStationTo(stationTo);
            ticket.setRoute(stationFrom+"&nbsp;→&nbsp;"+stationTo);
            ticket.setTrip(TicketHelper.getTrainInfo(trip));
            ticket.setTripId(tripId);
            Date depDate=TimeHelper.addHours(trip.getDepartureTime(),reFrom.getHour(),reFrom.getMinute());
            Date arrDate=TimeHelper.addHours(trip.getDepartureTime(),reTo.getHour(),reTo.getMinute());
            ticket.setDeparture(depDate);
            ticket.setArrival(arrDate);
            LOGGER.info("Поиск пользователя " + login + "в базе данных");
            User user = userRepository.findByLogin(login);
            if(user == null){
                LOGGER.error("Пользователь с логином " + login + " не найден в базе данных");
                throw new UserNotFoundException("Пользователь с логином " + login + " не найден в базе данных");
            }
            ticket.setUserName(user.getName());
            ticket.setLogin(login);
            ticket.setUserSurname(user.getSurname());
            ticket.setBirthDate(TimeHelper.getDatePart(user.getBirthDate()));

            double minutes = (ticket.getDeparture().getTime() - purchaseDate.getTime()) / (60 * 1000);
            if (minutes < 10) {
                LOGGER.error("Пользователь " + user.getLogin() + " попытался купить билет меньше, чем за 10 минут до отправления поезда");
                throw new TimeConstraintException("Вы не можете купить билет меньше, чем за 10 минут до отправления поезда");
            }
            if (ticketRepository.countByUser_NameAndUser_SurnameAndUser_BirthDateAndTrip_Id(ticket.getUserName(),ticket.getUserSurname(),
                    new SimpleDateFormat("dd.MM.yyyy").parse(ticket.getBirthDate()),ticket.getTripId()) > 0) {
                LOGGER.error("Пользователь " + user.getLogin() + " уже зарегистрирован на этот рейс");
                throw new UserAlreadyRegisteredException("Вы уже зарегистрированы на этот рейс");
            }
            ticket.setSeats(getAvaliableSeats(stationFrom,stationTo,trip.getId()));
            ticket.setTrainRate(trip.getTrain().getRate().getId());
            ticket.setRouteId(trip.getRoute().getId());
            List<ServiceTO> serviceTOs = new ArrayList<ServiceTO>();
            List<Service>serviceList=serviceRepository.findAll();
            for(Service s : serviceList){
                ServiceTO serviceTO = new ServiceTO();
                serviceTO.setId(s.getId());
                serviceTO.setName(s.getName());
                serviceTO.setValue(s.getValue());
                serviceTOs.add(serviceTO);
            }
            ticket.setServices(serviceTOs);
            Map<Long, String> rates=new HashMap<Long, String>();
            List<Rate> rateList=rateRepository.findByForTrainFalse();
            for(Rate r:rateList){
                rates.put((long)r.getId(),r.getName());
            }

            ticket.setRateTypes(rates);
        } catch (ParseException e) {
            LOGGER.error("Некорректный формат даты");
        }
        LOGGER.info("Оформление билета завершено");
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
    public double calcPrice(TicketTO ticket) throws TripDetailsNotFoundException {
        LOGGER.info("Вычисление цены");
        double price = 0;
        RouteEntry reFrom = routeEntryRepository.findByStation_NameAndRoute_Id(ticket.getStationFrom(), ticket.getRouteId());
        RouteEntry reTo = routeEntryRepository.findByStation_NameAndRoute_Id(ticket.getStationTo(), ticket.getRouteId());
        if (reFrom == null || reTo == null) {
            throw new TripDetailsNotFoundException("Информация о маршруте рейса " + ticket.getTripId() + "не найдена в базе данных");
        }
        double distance = reTo.getDistance() - reFrom.getDistance();

        if (ticket.getServices().size() > 0) {
            List<ServiceTO> services = ticket.getServices();
            for (ServiceTO k : services)
                price += k.getValue();
        }
        price += distance * rateRepository.findOne(ticket.getRateType()).getValue() *
                 rateRepository.findOne(ticket.getTrainRate()).getValue();
        LOGGER.info("Вычисленная цена: " + price);
        return price;
    }

    /**
     * Returns list of seat number that not bought.
     * @param stationFrom route begin.
     * @param stationTo route end.
     * @param tripId id of specified trip.
     * @return list of available seat numbers.
     */
    @PreAuthorize("isAuthenticated()")
    public List<Integer> getAvaliableSeats (String stationFrom, String stationTo, int tripId) throws TripNotFoundException {
        LOGGER.info("Получение списка доступных мест на маршрут с id=" + tripId);
        Trip trip = tripRepository.findOne(tripId);
        if (trip == null) {
            LOGGER.error("Рейса с номером " + tripId + " не существует в базе данных");
            throw new TripNotFoundException("Рейса с номером " + tripId + " не существует в базе данных");
        }
        List<Ticket> tickets = ticketRepository.findByTrip_Id(trip.getId());
        Map<String, Integer> stations = new HashMap<String, Integer>();
        List<RouteEntry> entries = routeEntryRepository.findByRoute_Id(trip.getRoute().getId());
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
    public List<PassengerTO> getPassengersByTrip(int tripId) throws TripNotFoundException {
        LOGGER.info("Получение списка пассажиров рейса " + tripId);
        Trip trip = tripRepository.findOne(tripId);
        if (trip == null) {
            LOGGER.error("Рейса с id=" + tripId + "не существует в базе данных");
            throw new TripNotFoundException("Рейса с id=" + tripId + "не существует в базе данных");
        }
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
        LOGGER.info("Найдено " + passengers.size() + " пассажиров");
        return passengers;
    }

    public List<TicketTO> getTickets(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            LOGGER.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        List<Ticket> tickets = ticketRepository.findByUser_Id(user.getId());
        List<TicketTO> result = new ArrayList<TicketTO>();
        for (Ticket ticket : tickets) {
            TicketTO ticketTO = new TicketTO();
            String stationFrom = ticket.getStationFrom().getName();
            String stationTo = ticket.getStationTo().getName();
            ticketTO.setRoute(stationFrom+"&nbsp;→&nbsp;"+stationTo);
            Trip trip = ticket.getTrip();
            ticketTO.setTrip(TicketHelper.getTrainInfo(trip));
            ticketTO.setTripId(trip.getId());
            int routeId = ticket.getTrip().getRoute().getId();
            RouteEntry reFrom = routeEntryRepository.findByStation_NameAndRoute_Id(stationFrom, routeId);
            RouteEntry reTo = routeEntryRepository.findByStation_NameAndRoute_Id(stationTo, routeId);
            Date depDate=TimeHelper.addHours(trip.getDepartureTime(),reFrom.getHour(),reFrom.getMinute());
            Date arrDate=TimeHelper.addHours(trip.getDepartureTime(),reTo.getHour(),reTo.getMinute());
            ticketTO.setDeparture(depDate);
            ticketTO.setArrival(arrDate);
            ticketTO.setPrice(ticket.getPrice());
            ticketTO.setSeatNumber(ticket.getSeat());
            ticketTO.setId(ticket.getId());
            result.add(ticketTO);
        }
        return result;
    }

    public TicketTO getTicket(int id, String login) throws TicketNotFoundException, UserNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            LOGGER.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        Ticket ticket = ticketRepository.findOne(id);
        if (ticket == null) {
            throw new TicketNotFoundException("Билет с нормером " + id + " не найден в базе данных");
        }
        TicketTO ticketTO = new TicketTO();
        String stationFrom = ticket.getStationFrom().getName();
        String stationTo = ticket.getStationTo().getName();
        ticketTO.setStationFrom(stationFrom);
        ticketTO.setStationTo(stationTo);
        ticketTO.setRoute(stationFrom+"&nbsp;→&nbsp;"+stationTo);
        Trip trip = ticket.getTrip();
        ticketTO.setTrip(TicketHelper.getTrainInfo(trip));
        ticketTO.setTripId(trip.getId());
        int routeId = ticket.getTrip().getRoute().getId();
        RouteEntry reFrom = routeEntryRepository.findByStation_NameAndRoute_Id(stationFrom, routeId);
        RouteEntry reTo = routeEntryRepository.findByStation_NameAndRoute_Id(stationTo, routeId);
        Date depDate=TimeHelper.addHours(trip.getDepartureTime(),reFrom.getHour(),reFrom.getMinute());
        Date arrDate=TimeHelper.addHours(trip.getDepartureTime(),reTo.getHour(),reTo.getMinute());
        ticketTO.setDeparture(depDate);
        ticketTO.setArrival(arrDate);
        ticketTO.setUserName(user.getName());
        ticketTO.setLogin(login);
        ticketTO.setUserSurname(user.getSurname());
        ticketTO.setBirthDate(TimeHelper.getDatePart(user.getBirthDate()));
        ticketTO.setPrice(ticket.getPrice());
        ticketTO.setSeatNumber(ticket.getSeat());
        ticketTO.setId(ticket.getId());
        ticketTO.setRateName(ticket.getRate().getName());
        List<Rate> rateList=rateRepository.findByForTrainFalse();
        Map<Long, String> rates=new HashMap<Long, String>();
        for(Rate r:rateList) {
            rates.put((long) r.getId(), r.getName());
        }
        ticketTO.setRateTypes(rates);
        List<Service> services = ticket.getServices();
        List<ServiceTO> serviceTOs = new ArrayList<ServiceTO>();
        if (services != null) {
            for (Service service : services) {
                ServiceTO serviceTO = new ServiceTO();
                serviceTO.setId(service.getId());
                serviceTO.setName(service.getName());
                serviceTO.setValue(service.getValue());
                serviceTOs.add(serviceTO);
            }
        }
        ticketTO.setServices(serviceTOs);
        return ticketTO;
    }

    public List<ServiceTO> completeServices(List<ServiceTO> services) {
        for (ServiceTO service : services) {
            Service s = serviceRepository.findOne(service.getId());
            service.setName(s.getName());
            service.setValue(s.getValue());
        }
        return services;
    }
}

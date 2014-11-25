//package ru.tsystems.tsproject.sbb2;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import ru.tsystems.tsproject.sbb.database.dao.*;
//import ru.tsystems.tsproject.sbb.database.entity.*;
//import ru.tsystems.tsproject.sbb.services.TicketService;
//import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
//import ru.tsystems.tsproject.sbb.services.exceptions.TimeConstraintException;
//import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyRegisteredException;
//import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.when;
//
///**
// * Implements set of tests for Ticket service.
// * @author Daria Nikiforova
// */
//@RunWith(MockitoJUnitRunner.class)
//public class TicketServiceTest {
//    @Mock
//    private TicketDAO ticketDAO;
//
//    @Mock
//    private StationDAO stationDAO;
//
//    @Mock
//    private TripDAO tripDAO;
//
//    @Mock
//    private RouteEntryDAO routeEntryDAO;
//
//    @Mock
//    private ServiceDAO serviceDAO;
//
//    @Mock
//    private RateDAO rateDAO;
//
//    @Mock
//    private UserDAO userDAO;
//
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private FactoryDAO factoryDAO;
//
//    @Mock
//    private EntityTransaction entityTransaction;
//
//    @InjectMocks
//    private TicketService ticketService = new TicketService(null, null, null, null, null, null, null, null);
//
//    /**
//     * Tests method TicketService.addTicket. Test passed when expected data equals to created.
//     * @throws Exception
//     */
//    @Test
//    public void testAddTicket_Success() throws Exception {
//        String name = "Will";
//        String surname = "Smith";
//        String birthDate = "1980.08.25 00:00:00";
//
//        String departureDate = "29.12.2014 00:10";
//        String arrivalDate = "29.12.2014 10:00";
//
//        String login = "will";
//        String password = "2222";
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//        int tripId = 2;
//        Station stationFrom = new Station();
//        stationFrom.setName("Udelnaya");
//        stationFrom.setId(1);
//
//        Station stationTo = new Station();
//        stationTo.setName("Repino");
//        stationTo.setId(3);
//
//        Date date = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(birthDate);
//
//        User user = new User();
//        user.setId(1);
//        user.setBirthDate(date);
//        user.setName(name);
//        user.setLogin(login);
//        user.setPassword(password);
//        user.setRole(role);
//        user.setSurname(surname);
//        user.setId(1);
//
//        Date depDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(departureDate);
//        Date arrDate =  new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(arrivalDate);
//        Trip trip = new Trip();
//        trip.setId(2);
//        trip.setDepartureTime(depDate);
//        trip.setRoute(trip.getRoute());
//
//        Service service1 = new Service();
//
//        service1.setId(1);
//        service1.setName("standard package");
//
//        Service service2 = new Service();
//
//        service2.setId(2);
//        service2.setName("business package");
//
//        Rate rate = new Rate();
//        rate.setId(2);
//        rate.setName("adult");
//
//        TicketTO ticket = new TicketTO();
//        ticket.setPrice(1000);
//        ticket.setBirthDate(birthDate);
//        ticket.setSeatNumber(2);
//        ticket.setDeparture(depDate);
//        ticket.setArrival(arrDate);
//        ticket.setStationFrom(stationFrom.getName());
//        ticket.setStationTo(stationTo.getName());
//        ticket.setLogin(user.getLogin());
//        ticket.setUserSurname(surname);
//        ticket.setUserName(name);
//        ticket.setRateType(rate.getId());
//        Map<Long,String> services = new HashMap<Long, String>();
//        services.put((long) service1.getId(), service1.getName());
//        services.put((long) service2.getId(), service2.getName());
//        ticket.setServices(services);
//        ticket.setTripId(tripId);
//
//        Ticket t = new Ticket();
//        t.setPrice(ticket.getPrice());
//        t.setSeat(ticket.getSeatNumber());
//        Station s = new Station();
//        s.setName(stationFrom.getName());
//        s.setId(stationFrom.getId());
//        t.setStationFrom(s);
//        s.setName(stationTo.getName());
//        s.setId(stationTo.getId());
//        t.setStationTo(s);
//        t.setUser(user);
//        t.setTrip(trip);
//        t.setRate(rate);
//        List<Service> serviceList = new ArrayList<Service>();
//        serviceList.add(service1);
//        serviceList.add(service2);
//
//        doReturn(false).when(ticketDAO).isUserExist(name, surname, date, tripId);
//        when(stationDAO.getStation(stationFrom.getName())).thenReturn(stationFrom);
//        when(stationDAO.getStation(stationTo.getName())).thenReturn(stationTo);
//        when(userDAO.getUser(login)).thenReturn(user);
//        when(tripDAO.getTrip(tripId)).thenReturn(trip);
//        when(serviceDAO.getServiceById(service1.getId())).thenReturn(service1);
//        when(serviceDAO.getServiceById(service2.getId())).thenReturn(service2);
//        when(ticketDAO.getTicket(login, tripId)).thenReturn(t);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//
//        ticketService.AddTicket(ticket);
//        Ticket createdTicket = ticketDAO.getTicket(ticket.getLogin(), ticket.getTripId());
//        Assert.assertEquals(t.getPrice(), 0.0001, createdTicket.getPrice());
//        Assert.assertEquals(t.getSeat(), createdTicket.getSeat());
//        Assert.assertEquals(t.getRate().getId(), createdTicket.getRate().getId());
//        Assert.assertEquals(t.getStationFrom().getId(), t.getStationFrom().getId());
//        for (int i = 0; i < t.getServices().size(); i++) {
//            Assert.assertEquals(t.getServices().get(i).getId(), createdTicket.getServices().get(i).getId());
//            Assert.assertEquals(t.getServices().get(i).getName(), createdTicket.getServices().get(i).getName());
//        }
//    }
//
//    /**
//     * Tests method TicketService.addStation. Test passed when throws UserAlreadyRegisterException.
//     * @throws Exception
//     */
//    @Test(expected = UserAlreadyRegisteredException.class)
//    public void testAddTicket_UserAlreadyRegisterException() throws Exception {
//        String name = "Will";
//        String surname = "Smith";
//        String birthDate = "1980.08.25 00:00:00";
//        String departureDate = "29.12.2014 00:10";
//        String arrivalDate = "29.12.2014 10:00";
//        String login = "will";
//        String password = "2222";
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//        int tripId = 2;
//
//        Station stationFrom = new Station();
//        stationFrom.setName("Udelnaya");
//        stationFrom.setId(1);
//
//        Station stationTo = new Station();
//        stationTo.setName("Repino");
//        stationTo.setId(3);
//
//        Date date = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(birthDate);
//
//        User user = new User();
//        user.setId(1);
//        user.setBirthDate(date);
//        user.setName(name);
//        user.setLogin(login);
//        user.setPassword(password);
//        user.setRole(role);
//        user.setSurname(surname);
//        user.setId(1);
//
//        Date depDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(departureDate);
//        Date arrDate =  new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(arrivalDate);
//        Trip trip = new Trip();
//        trip.setId(2);
//        trip.setDepartureTime(depDate);
//        trip.setRoute(trip.getRoute());
//
//        Service service1 = new Service();
//        service1.setId(1);
//        service1.setName("standard package");
//
//        Service service2 = new Service();
//        service2.setId(2);
//        service2.setName("business package");
//
//        Rate rate = new Rate();
//        rate.setId(2);
//        rate.setName("adult");
//
//        TicketTO ticket = new TicketTO();
//        ticket.setPrice(1000);
//        ticket.setBirthDate(birthDate);
//        ticket.setSeatNumber(2);
//        ticket.setDeparture(depDate);
//        ticket.setArrival(arrDate);
//        ticket.setStationFrom(stationFrom.getName());
//        ticket.setStationTo(stationTo.getName());
//        ticket.setLogin(user.getLogin());
//        ticket.setUserSurname(surname);
//        ticket.setUserName(name);
//        ticket.setRateType(rate.getId());
//        Map<Long,String> services = new HashMap<Long, String>();
//        services.put((long) service1.getId(), service1.getName());
//        services.put((long) service2.getId(), service2.getName());
//        ticket.setServices(services);
//        ticket.setTripId(tripId);
//
//        Ticket t = new Ticket();
//        t.setPrice(ticket.getPrice());
//        t.setSeat(ticket.getSeatNumber());
//        Station s = new Station();
//        s.setName(stationFrom.getName());
//        s.setId(stationFrom.getId());
//        t.setStationFrom(s);
//        s.setName(stationTo.getName());
//        s.setId(stationTo.getId());
//        t.setStationTo(s);
//        t.setUser(user);
//        t.setTrip(trip);
//        t.setRate(rate);
//        List<Service> serviceList = new ArrayList<Service>();
//        serviceList.add(service1);
//        serviceList.add(service2);
//
//        doReturn(true).when(ticketDAO).isUserExist(name, surname, date, tripId);
//        when(stationDAO.getStation(stationFrom.getName())).thenReturn(stationFrom);
//        when(stationDAO.getStation(stationTo.getName())).thenReturn(stationTo);
//        when(userDAO.getUser(login)).thenReturn(user);
//        when(tripDAO.getTrip(tripId)).thenReturn(trip);
//        when(serviceDAO.getServiceById(service1.getId())).thenReturn(service1);
//        when(serviceDAO.getServiceById(service2.getId())).thenReturn(service2);
//        when(ticketDAO.getTicket(login, tripId)).thenReturn(t);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//
//        ticketService.AddTicket(ticket);
//    }
//
//    /**
//     * Tests method TicketService.addStation. Test passed when throws TimeConstraintException.
//     * @throws Exception
//     */
//    @Test(expected = TimeConstraintException.class)
//    public void testAddTicket_TimeConstraintException() throws Exception {
//        String name = "Will";
//        String surname = "Smith";
//        String birthDate = "1980.08.25 00:00:00";
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.add(Calendar.MINUTE, 5);
//        Date d = c.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
//        String departureDate = sdf.format(d);
//        String arrivalDate = "29.12.2014 10:00";
//
//        String login = "will";
//        String password = "2222";
//        Role role = new Role();
//        role.setId(2);
//        role.setName("client");
//        int tripId = 2;
//        Station stationFrom = new Station();
//        stationFrom.setName("Udelnaya");
//        stationFrom.setId(1);
//
//        Station stationTo = new Station();
//        stationTo.setName("Repino");
//        stationTo.setId(3);
//
//        Date date = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(birthDate);
//
//        User user = new User();
//        user.setId(1);
//        user.setBirthDate(date);
//        user.setName(name);
//        user.setLogin(login);
//        user.setPassword(password);
//        user.setRole(role);
//        user.setSurname(surname);
//        user.setId(1);
//
//        Date depDate = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(departureDate);
//        Date arrDate =  new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(arrivalDate);
//        Trip trip = new Trip();
//        trip.setId(2);
//        trip.setDepartureTime(depDate);
//        trip.setRoute(trip.getRoute());
//
//        Service service1 = new Service();
//
//        service1.setId(1);
//        service1.setName("standard package");
//
//        Service service2 = new Service();
//
//        service2.setId(2);
//        service2.setName("business package");
//
//        Rate rate = new Rate();
//        rate.setId(2);
//        rate.setName("adult");
//
//        TicketTO ticket = new TicketTO();
//        ticket.setPrice(1000);
//        ticket.setBirthDate(birthDate);
//        ticket.setSeatNumber(2);
//        ticket.setDeparture(depDate);
//        ticket.setArrival(arrDate);
//        ticket.setStationFrom(stationFrom.getName());
//        ticket.setStationTo(stationTo.getName());
//        ticket.setLogin(user.getLogin());
//        ticket.setUserSurname(surname);
//        ticket.setUserName(name);
//        ticket.setRateType(rate.getId());
//        Map<Long,String> services = new HashMap<Long, String>();
//        services.put((long) service1.getId(), service1.getName());
//        services.put((long) service2.getId(), service2.getName());
//        ticket.setServices(services);
//        ticket.setTripId(tripId);
//
//        Ticket t = new Ticket();
//        t.setPrice(ticket.getPrice());
//        t.setSeat(ticket.getSeatNumber());
//        Station s = new Station();
//        s.setName(stationFrom.getName());
//        s.setId(stationFrom.getId());
//        t.setStationFrom(s);
//        s.setName(stationTo.getName());
//        s.setId(stationTo.getId());
//        t.setStationTo(s);
//        t.setUser(user);
//        t.setTrip(trip);
//        t.setRate(rate);
//        List<Service> serviceList = new ArrayList<Service>();
//        serviceList.add(service1);
//        serviceList.add(service2);
//
//        doReturn(false).when(ticketDAO).isUserExist(name, surname, date, tripId);
//        when(stationDAO.getStation(stationFrom.getName())).thenReturn(stationFrom);
//        when(stationDAO.getStation(stationTo.getName())).thenReturn(stationTo);
//        when(userDAO.getUser(login)).thenReturn(user);
//        when(tripDAO.getTrip(tripId)).thenReturn(trip);
//        when(serviceDAO.getServiceById(service1.getId())).thenReturn(service1);
//        when(serviceDAO.getServiceById(service2.getId())).thenReturn(service2);
//        when(ticketDAO.getTicket(login, tripId)).thenReturn(t);
//        when(entityManager.getTransaction()).thenThrow(TimeConstraintException.class);
//
//        ticketService.AddTicket(ticket);
//    }
//
//    /**
//     * Tests method TicketService.calcPrice. Test passed when expected data equals to created.
//     * @throws Exception
//     */
//    @Test
//    public void testCalcPrice_Success() throws Exception {
//        int tripId = 2;
//        Station stationFrom = new Station();
//        stationFrom.setName("Udelnaya");
//        stationFrom.setId(1);
//
//        Station stationTo = new Station();
//        stationTo.setName("Repino");
//        stationTo.setId(3);
//
//        int routeId = 2;
//
//        TicketTO parameterTicket = new TicketTO();
//        parameterTicket.setStationFrom(stationFrom.getName());
//        parameterTicket.setStationTo(stationTo.getName());
//        parameterTicket.setRouteId(routeId);
//        parameterTicket.setTripId(tripId);
//
//        RouteEntry reFrom = new RouteEntry();
//        reFrom.setStation(stationFrom);
//        reFrom.setDistance(100);
//
//        RouteEntry reTo = new RouteEntry();
//        reTo.setStation(stationTo);
//        reTo.setDistance(200);
//
//        Rate rate1 = new Rate();
//        rate1.setId(2);
//        rate1.setName("adult");
//
//        Rate rate2 = new Rate();
//        rate2.setId(1);
//        rate2.setName("express");
//
//        when(routeEntryDAO.getEntry(parameterTicket.getStationFrom(), parameterTicket.getTripId())).thenReturn(reFrom);
//        when(routeEntryDAO.getEntry(parameterTicket.getStationTo(), parameterTicket.getTripId())).thenReturn(reTo);
//        double distance = reTo.getDistance() - reFrom.getDistance();
//
//        Service service1 = new Service();
//
//        service1.setId(1);
//        service1.setName("standard package");
//        service1.setValue(250);
//
//        Service service2 = new Service();
//
//        service2.setId(2);
//        service2.setName("business package");
//        service2.setValue(300);
//
//        Map<Long,String> services = new HashMap<Long, String>();
//        services.put((long) service1.getId(), service1.getName());
//        services.put((long) service2.getId(), service2.getName());
//        parameterTicket.setServices(services);
//
//        when(serviceDAO.getValueById(service1.getId())).thenReturn(service1.getValue());
//        when(serviceDAO.getValueById(service2.getId())).thenReturn(service2.getValue());
//        when(rateDAO.getValueById(parameterTicket.getRateType())).thenReturn(rate1.getValue());
//        when(rateDAO.getValueById(parameterTicket.getTrainRate())).thenReturn(rate2.getValue());
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        Double price = 0.0;
//        for (Long k : services.keySet())
//            price += serviceDAO.getValueById(k.intValue());
//        Double expectedPrice = price + distance*rate1.getValue()*rate2.getValue();
//        price = ticketService.calcPrice(parameterTicket);
//        Assert.assertEquals(expectedPrice, 0.0001, price);
//    }
//
//    /**
//     * Tests method TicketService.calcPrice. Test passed when throws PersistenceException.
//     * @throws Exception
//     */
//    @Test(expected = ServiceException.class)
//    public void testCalcPrice_NullArgument() throws Exception {
//        int tripId = 2;
//        Station stationFrom = new Station();
//
//        Station stationTo = new Station();
//        stationTo.setName("Repino");
//        stationTo.setId(3);
//
//        int routeId = 2;
//
//        TicketTO parameterTicket = new TicketTO();
//        parameterTicket.setStationFrom(stationFrom.getName());
//        parameterTicket.setStationTo(stationTo.getName());
//        parameterTicket.setRouteId(routeId);
//        parameterTicket.setTripId(tripId);
//
//        RouteEntry reFrom = new RouteEntry();
//        reFrom.setStation(stationFrom);
//        reFrom.setDistance(0);
//
//        RouteEntry reTo = new RouteEntry();
//        reTo.setStation(stationTo);
//        reTo.setDistance(0);
//
//        Rate rate1 = new Rate();
//        rate1.setId(2);
//        rate1.setName("adult");
//
//        Rate rate2 = new Rate();
//        rate2.setId(1);
//        rate2.setName("express");
//
//        when(routeEntryDAO.getEntry(parameterTicket.getStationFrom(), parameterTicket.getTripId())).thenThrow(PersistenceException.class);
//        when(routeEntryDAO.getEntry(parameterTicket.getStationTo(), parameterTicket.getTripId())).thenReturn(reTo);
//        double distance = reTo.getDistance() - reFrom.getDistance();
//
//        Service service1 = new Service();
//
//        service1.setId(1);
//        service1.setName("standard package");
//        service1.setValue(0);
//
//        Service service2 = new Service();
//
//        service2.setId(2);
//        service2.setName("business package");
//        service2.setValue(0);
//
//        Map<Long,String> services = new HashMap<Long, String>();
//        services.put((long) service1.getId(), service1.getName());
//        services.put((long) service2.getId(), service2.getName());
//        parameterTicket.setServices(services);
//
//        when(serviceDAO.getValueById(service1.getId())).thenReturn(service1.getValue());
//        when(serviceDAO.getValueById(service2.getId())).thenReturn(service2.getValue());
//        when(rateDAO.getValueById(parameterTicket.getRateType())).thenReturn(rate1.getValue());
//        when(rateDAO.getValueById(parameterTicket.getTrainRate())).thenReturn(rate2.getValue());
//        when(entityManager.getTransaction()).thenThrow(PersistenceException.class);
//        Double price = 0.0;
//        for (Long k : services.keySet())
//            price += serviceDAO.getValueById(k.intValue());
//        Double expectedPrice = price + distance*rate1.getValue()*rate2.getValue();
//        price = ticketService.calcPrice(parameterTicket);
//        Assert.assertEquals(expectedPrice, 0.0001, price);
//    }
//}

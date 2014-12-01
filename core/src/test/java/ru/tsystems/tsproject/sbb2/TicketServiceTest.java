package ru.tsystems.tsproject.sbb2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.tsystems.tsproject.sbb.database.entity.Role;
import ru.tsystems.tsproject.sbb.database.repositories.*;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.exceptions.SeatAlreadyRegisteredException;
import ru.tsystems.tsproject.sbb.services.exceptions.TimeConstraintException;
import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyRegisteredException;
import ru.tsystems.tsproject.sbb.transferObjects.TicketTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.when;

/**
 * Implements set of tests for Ticket service.
 * @author Daria Nikiforova
 */
@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {
    @Mock private TicketRepository ticketRepository;
    @Mock private StationRepository stationRepository;
    @Mock private TripRepository tripRepository;
    @Mock private RouteEntryRepository routeEntryRepository;
    @Mock private ServiceRepository serviceRepository;
    @Mock private RateRepository rateRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private TicketService ticketService = new TicketService();

    private TicketTO ticketTO;

    @Before
    public void setup() throws ParseException {
        Role role = new Role();
        role.setId(2);
        role.setName("client");

        String name = "Will";
        String surname = "Smith";
        int tripId = 0;
        int seatNumber = 100;
        String birthDate = "1980.08.25 00:00:00";
        String arrivalDate = "31.12.2014 23:59";
        String departureDate = "31.12.2014 00:00";

        ticketTO = new TicketTO();
        ticketTO.setTripId(tripId);
        ticketTO.setSeatNumber(seatNumber);
        ticketTO.setUserName(name);
        ticketTO.setUserSurname(surname);
        ticketTO.setBirthDate(birthDate);
        ticketTO.setArrival(new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(arrivalDate));
        ticketTO.setDeparture(new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(departureDate));
    }

    @Test(expected = SeatAlreadyRegisteredException.class)
    public void testAddTicket_SeatAlreadyRegistered() throws Exception {
        when(ticketRepository.countByTripIdAndSeat(ticketTO.getTripId(), ticketTO.getSeatNumber())).thenReturn(new Long(1));
        ticketService.AddTicket(ticketTO);
    }

    @Test(expected = TimeConstraintException.class)
    public void testAddTicket_TimeConstraint() throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, 5);
        Date date = c.getTime();
        when(ticketRepository.countByTripIdAndSeat(ticketTO.getTripId(), ticketTO.getSeatNumber())).thenReturn(new Long(0));
        ticketService.AddTicket(withChangedTicket(ticketTO, date));
    }

    @Test(expected = UserAlreadyRegisteredException.class)
    public void testAddTicket_UserAlreadyRegistered() throws Exception {
        when(ticketRepository.countByTripIdAndSeat(ticketTO.getTripId(), ticketTO.getSeatNumber())).thenReturn(new Long(0));
        when(ticketRepository.countByUser_NameAndUser_SurnameAndUser_BirthDateAndTrip_Id(
                ticketTO.getUserName(), ticketTO.getUserSurname(),
                new SimpleDateFormat("dd.MM.yyyy").parse(ticketTO.getBirthDate()), ticketTO.getTripId()))
                .thenReturn(new Long(1));
        ticketService.AddTicket(ticketTO);
    }

    private TicketTO withChangedTicket(TicketTO oldTicket, Date departureDate) {
        TicketTO ticketTO = new TicketTO();
        ticketTO.setTripId(oldTicket.getTripId());
        ticketTO.setSeatNumber(oldTicket.getSeatNumber());
        ticketTO.setUserName(oldTicket.getUserName());
        ticketTO.setUserSurname(oldTicket.getUserSurname());
        ticketTO.setBirthDate(oldTicket.getBirthDate());
        ticketTO.setArrival(oldTicket.getArrival());
        ticketTO.setDeparture(departureDate);
        return ticketTO;
    }
}

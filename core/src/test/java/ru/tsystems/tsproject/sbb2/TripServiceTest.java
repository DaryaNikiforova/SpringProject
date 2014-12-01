package ru.tsystems.tsproject.sbb2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.tsystems.tsproject.sbb.database.entity.*;
import ru.tsystems.tsproject.sbb.database.repositories.*;
import ru.tsystems.tsproject.sbb.services.TicketService;
import ru.tsystems.tsproject.sbb.services.TripService;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.services.exceptions.TripNotFoundException;
import ru.tsystems.tsproject.sbb.services.helpers.RouteHelper;
import ru.tsystems.tsproject.sbb.services.helpers.TimeHelper;
import ru.tsystems.tsproject.sbb.transferObjects.TimetableTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Implements set of tests for Trip service.
 * @author Daria Nikiforova
 */

@RunWith(MockitoJUnitRunner.class)
public class TripServiceTest {
    @Mock RouteEntryRepository routeEntryRepository;
    @Mock TripRepository tripRepository;
    @Mock TicketService ticketService;
    @Mock StationRepository stationRepository;
    @Mock TrainRepository trainRepository;
    @Mock RouteRepository routeRepository;
    @Mock TicketRepository ticketRepository;
    @InjectMocks private TripService tripService = new TripService();

    final private String MOSCOW = "Moscow";
    final private String SAINT_PETERSBURG = "Saint-Petersburg";
    private Date date;
    private Date dateFrom;
    private Date date31dec;
    private Date dateTo;
    public Station stationFrom;
    public Station stationTo;
    Route mockRoute;
    Rate mockRate;
    Train mockTrain;
    List<RouteEntry> mockEntries;
    List<Trip> mockTrips;
    List<Integer> mockSeats;

    @Before
    public void setup() throws ParseException {
        date = new SimpleDateFormat("dd.MM.yyyy").parse("02.01.2015");
        dateFrom = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2015");
        dateTo = new SimpleDateFormat("dd.MM.yyyy").parse("03.01.2015");
        date31dec = new SimpleDateFormat("dd.MM.yyyy").parse("31.12.2014");

        stationFrom = new Station();
        stationFrom.setId(0);
        stationFrom.setName(MOSCOW);

        stationTo = new Station();
        stationTo.setId(1);
        stationTo.setName(SAINT_PETERSBURG);

        mockRate = new Rate();
        mockRate.setId(0);
        mockRate.setName("Rate1");
        mockRate.setForTrain(true);
        mockRate.setValue(10);

        mockTrain = new Train();
        mockTrain.setId(0);
        mockTrain.setName("Allegro");
        mockTrain.setRate(mockRate);
        mockTrain.setSeatCount(100);

        mockRoute = new Route();
        mockRoute.setId(0);

        mockEntries = new ArrayList<RouteEntry>();
        RouteEntry routeEntry = new RouteEntry();
        routeEntry.setId(0);
        routeEntry.setDistance(0);
        routeEntry.setHour(0);
        routeEntry.setMinute(0);
        routeEntry.setSeqNumber(1);
        routeEntry.setRoute(mockRoute);
        routeEntry.setStation(stationFrom);
        mockEntries.add(routeEntry);

        routeEntry = new RouteEntry();
        routeEntry.setId(1);
        routeEntry.setDistance(1);
        routeEntry.setHour(1);
        routeEntry.setMinute(0);
        routeEntry.setSeqNumber(2);
        routeEntry.setRoute(mockRoute);
        routeEntry.setStation(stationTo);
        mockEntries.add(routeEntry);
        mockRoute.setRouteEntries(mockEntries);

        mockTrips = new ArrayList<Trip>();
        Trip trip = new Trip();
        trip.setId(0);
        trip.setDepartureTime(date);
        trip.setRoute(mockRoute);
        trip.setTrain(mockTrain);
        mockTrips.add(trip);

        mockSeats = new ArrayList<Integer>();
        mockSeats.add(1);
    }

    @Test
    public void testSearchTrains_Success() throws Exception {
        when(stationRepository.findByName(stationFrom.getName())).thenReturn(stationFrom);
        when(stationRepository.findByName(stationTo.getName())).thenReturn(stationTo);
        when(routeEntryRepository.findByStations(MOSCOW, SAINT_PETERSBURG)).thenReturn(mockEntries);
        when(tripRepository.findByRoute_IdAndDepartureTimeBetween(mockRoute.getId(),
                TimeHelper.getHourBack(dateFrom, mockEntries.get(0).getHour(), mockEntries.get(0).getMinute()),
                TimeHelper.getHourBack(dateTo, mockEntries.get(0).getHour(), mockEntries.get(0).getMinute()))).
                thenReturn(mockTrips);
        when(routeEntryRepository.findByStation_NameAndRoute_Id(stationTo.getName(), mockRoute.getId()))
                .thenReturn(mockEntries.get(0));
        when(ticketService.getAvaliableSeats(stationFrom.getName(), stationTo.getName(), mockTrips.get(0).getId()))
                .thenReturn(mockSeats);

        List<TimetableTO> timetableTOs = tripService.searchTrains(stationFrom.getName(), stationTo.getName(), dateFrom, dateTo);

        assertEquals(timetableTOs.size(), 1);
        for(TimetableTO timetableTO : timetableTOs) {
            assertEquals(timetableTO.getSeatCount(), 1);
            assertEquals(timetableTO.getTrainNumber(), mockTrips.get(0).getTrain().getId());
            assertEquals(timetableTO.getTripId(), mockTrips.get(0).getId());
            assertEquals(timetableTO.getDepDate(), TimeHelper.addHours(mockTrips.get(0).getDepartureTime(),
                    mockEntries.get(0).getHour(), mockEntries.get(0).getMinute()));
            assertEquals(timetableTO.getArriveDate(), TimeHelper.addHours(mockTrips.get(0).getDepartureTime(),
                    mockEntries.get(0).getHour(), mockEntries.get(1).getMinute()));
            assertEquals(timetableTO.getDistance(), RouteHelper.getRouteDistance(mockTrips.get(0).getRoute()));
            assertEquals(timetableTO.getRouteName(), RouteHelper.getRouteName(mockTrips.get(0).getRoute()));
            assertEquals(timetableTO.getTime(),
                    RouteHelper.getRouteTime(mockTrips.get(0).getRoute(), stationFrom.getName(), stationTo.getName()));
            assertEquals(timetableTO.getStationFrom(), stationFrom.getName());
            assertEquals(timetableTO.getStationTo(), stationTo.getName());
        }
    }

    @Test(expected = StationNotFoundException.class)
    public void testSearchTrains_StationNotFound()  throws Exception {
        when(stationRepository.findByName(stationFrom.getName())).thenReturn(null);
        when(stationRepository.findByName(stationTo.getName())).thenReturn(null);
        tripService.searchTrains(stationFrom.getName(), stationTo.getName(), dateTo, dateFrom);
    }

    @Test(expected = TripNotFoundException.class)
    public void testSearchTrains_TripNotFound()  throws Exception {
        when(stationRepository.findByName(stationFrom.getName())).thenReturn(stationFrom);
        when(stationRepository.findByName(stationTo.getName())).thenReturn(stationTo);
        when(routeEntryRepository.findByStations(MOSCOW, SAINT_PETERSBURG)).thenReturn(mockEntries);
        when(tripRepository.findByRoute_IdAndDepartureTimeBetween(mockRoute.getId(),
                TimeHelper.getHourBack(dateFrom, mockEntries.get(0).getHour(), mockEntries.get(0).getMinute()),
                TimeHelper.getHourBack(dateTo, mockEntries.get(0).getHour(), mockEntries.get(0).getMinute()))).
                thenReturn(mockTrips);
        when(routeEntryRepository.findByStation_NameAndRoute_Id(stationTo.getName(), mockRoute.getId()))
                .thenReturn(mockEntries.get(0));
        when(ticketService.getAvaliableSeats(stationFrom.getName(), stationTo.getName(), mockTrips.get(0).getId()))
                .thenThrow(TripNotFoundException.class);

        tripService.searchTrains(stationFrom.getName(), stationTo.getName(), dateFrom, dateTo);
    }

    @Test
    public void testGetRoutesByStation_Success() throws Exception {
        when(stationRepository.findByName(stationFrom.getName())).thenReturn(stationFrom);
        when(routeEntryRepository.findByStation_Name(stationFrom.getName())).thenReturn(mockEntries);
        when(tripRepository.findByRoute_IdAndDepartureTimeBetween(mockRoute.getId(), mockTrips.get(0).getDepartureTime(),
                TimeHelper.addHours(mockTrips.get(0).getDepartureTime(), 23, 59))).thenReturn(mockTrips);
        when(routeEntryRepository.findLastByRoute_Id(mockRoute.getId())).thenReturn(mockEntries.get(1));

        List<TimetableTO> timetableTOs = tripService.getRoutesByStation(stationFrom.getName(), date);

        assertEquals(timetableTOs.size(), 1);
        for(TimetableTO timetableTO : timetableTOs) {
            assertEquals(timetableTO.getTrainNumber(), mockTrips.get(0).getTrain().getId());
            assertEquals(timetableTO.getTripId(), mockTrips.get(0).getId());
            assertEquals(timetableTO.getDepDate(), TimeHelper.addHours(mockTrips.get(0).getDepartureTime(),
                    mockEntries.get(0).getHour(), mockEntries.get(0).getMinute()));
            assertEquals(timetableTO.getArriveDate(), TimeHelper.addHours(mockTrips.get(0).getDepartureTime(),
                    mockEntries.get(1).getHour(), mockEntries.get(1).getMinute()));
            assertEquals(timetableTO.getRouteName(), RouteHelper.getRouteName(mockTrips.get(0).getRoute()));
            assertEquals(timetableTO.getStationFrom(), stationFrom.getName());
            assertEquals(timetableTO.getStationTo(), stationTo.getName());
        }
    }

    @Test(expected = StationNotFoundException.class)
    public void testGetRoutesByStation_Failed() throws Exception {
        when(stationRepository.findByName(stationFrom.getName())).thenReturn(null);
        tripService.getRoutesByStation(stationFrom.getName(), date);
    }

    @Test
    public void test_Success() throws Exception {}
}

package ru.tsystems.tsproject.sbb2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.AutoPopulatingList;
import ru.tsystems.tsproject.sbb.database.entity.Route;
import ru.tsystems.tsproject.sbb.database.entity.RouteEntry;
import ru.tsystems.tsproject.sbb.database.entity.Station;
import ru.tsystems.tsproject.sbb.database.repositories.RouteEntryRepository;
import ru.tsystems.tsproject.sbb.database.repositories.RouteRepository;
import ru.tsystems.tsproject.sbb.database.repositories.StationRepository;
import ru.tsystems.tsproject.sbb.services.RouteService;
import ru.tsystems.tsproject.sbb.services.exceptions.RouteNotFoundException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.services.helpers.RouteHelper;
import ru.tsystems.tsproject.sbb.transferObjects.RouteEntryTO;
import ru.tsystems.tsproject.sbb.transferObjects.RouteTO;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Implements set of tests for Train service.
 * @author Daria Nikiforova
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;
    @Mock
    RouteEntryRepository routeEntryRepository;
    @Mock
    StationRepository stationRepository;
    @InjectMocks
    private RouteService routeService = new RouteService();

    final private String MOSCOW = "Moscow";
    final private String SAINT_PETERSBURG = "Saint-Petersburg";
    List<RouteEntry> mockEntries;
    AutoPopulatingList<RouteEntryTO> mockEntryTOs;
    List<Route> mockRoutes;
    List<RouteTO> mockRouteTOs;
    private List<Station> mockStations;

    @Before
    public void setup() {
        //Stations
        mockStations = new ArrayList<Station>();
        Station station = new Station();
        station.setId(0);
        station.setName(MOSCOW);
        mockStations.add(station);

        station = new Station();
        station.setId(1);
        station.setName(SAINT_PETERSBURG);
        mockStations.add(station);

        //Routes
        mockRoutes = new ArrayList<Route>();
        Route route = new Route();
        route.setId(0);

        //Route entries
        mockEntries = new ArrayList<RouteEntry>();
        RouteEntry routeEntry = new RouteEntry();
        routeEntry.setId(0);
        routeEntry.setDistance(0);
        routeEntry.setHour(0);
        routeEntry.setMinute(0);
        routeEntry.setSeqNumber(1);
        routeEntry.setRoute(route);
        routeEntry.setStation(mockStations.get(0));
        mockEntries.add(routeEntry);

        routeEntry = new RouteEntry();
        routeEntry.setId(1);
        routeEntry.setDistance(1);
        routeEntry.setHour(1);
        routeEntry.setMinute(0);
        routeEntry.setSeqNumber(2);
        routeEntry.setRoute(route);
        routeEntry.setStation(mockStations.get(1));
        mockEntries.add(routeEntry);

        //Routes
        route.setRouteEntries(mockEntries);
        mockRoutes.add(route);

        //RouteTOs
        mockRouteTOs = new ArrayList<RouteTO>();
        RouteTO routeTO = new RouteTO();
        routeTO.setNumber(route.getId());
        routeTO.setDistance(RouteHelper.getRouteDistance(route));
        routeTO.setRoute(RouteHelper.getRouteName(route));
        routeTO.setTime(RouteHelper.getRouteTime(route));

        //RouteEntriesTOs
        mockEntryTOs = new AutoPopulatingList<RouteEntryTO>(RouteEntryTO.class);
        for(RouteEntry entry : mockEntries) {
            RouteEntryTO entryTO = new RouteEntryTO();
            entryTO.setStationName(entry.getStation().getName());
            entryTO.setSeqNumber(entry.getSeqNumber());
            entryTO.setDistance((int) entry.getDistance());
            entryTO.setHour(entry.getHour());
            entryTO.setMinute(entry.getMinute());
            mockEntryTOs.add(entryTO);
        }

        //RouteTOs
        routeTO.setRouteEntries(mockEntryTOs);
        mockRouteTOs.add(routeTO);
    }

    @Test
    public void testGetAllRoutes_Success() throws Exception {
        when(routeRepository.findAll()).thenReturn(mockRoutes);
        List<RouteTO> result = routeService.getAllRoutes();
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getDistance(), mockRouteTOs.get(i).getDistance());
            assertEquals(result.get(i).getNumber(), mockRouteTOs.get(i).getNumber());
            assertEquals(result.get(i).getRoute(), mockRouteTOs.get(i).getRoute());
            assertEquals(result.get(i).getTime(), mockRouteTOs.get(i).getTime());
        }
    }

    @Test
    public void testAddRoute_Success() throws Exception {
        for(RouteEntry entry : mockEntries) {
            when(stationRepository.findByName(entry.getStation().getName())).thenReturn(entry.getStation());
        }
        routeService.addRoute(mockRouteTOs.get(0));

        ArgumentCaptor<Route> captor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository).save(captor.capture());
        Route route = captor.getValue();
        for(int i = 0; i < route.getRouteEntries().size(); i++) {
            RouteEntry entry = route.getRouteEntries().get(i);
            assertEquals(entry.getRoute(), route);
            assertEquals(entry.getDistance(), mockEntries.get(i).getDistance(), 0.02);
            assertEquals(entry.getHour(), mockEntries.get(i).getHour());
            assertEquals(entry.getMinute(), mockEntries.get(i).getMinute());
            assertEquals(entry.getSeqNumber(), mockEntries.get(i).getSeqNumber());
            assertEquals(entry.getStation(), mockEntries.get(i).getStation());
        }
    }

    @Test
    public void testGetRoutesByStation_Success() throws Exception {
        when(stationRepository.findOne(mockStations.get(0).getId())).thenReturn(mockStations.get(0));
        when(routeEntryRepository.findByStation_Id(mockStations.get(0).getId())).thenReturn(withList(mockEntries.get(0)));
        List<RouteTO> routeTOs = routeService.getRoutesByStation(mockStations.get(0).getId());
        assertEquals(routeTOs.size(), 1);
        assertEquals(routeTOs.get(0).getNumber(), mockEntries.get(0).getRoute().getId());
    }

    @Test(expected = StationNotFoundException.class)
    public void testGetRoutesByStation_Failed() throws Exception {
        when(stationRepository.findOne(mockStations.get(0).getId())).thenReturn(null);
        routeService.getRoutesByStation(mockStations.get(0).getId());
    }

    @Test
    public void testDeleteRoute_Success() throws Exception {
        when(routeRepository.findOne(mockRouteTOs.get(0).getNumber())).thenReturn(mockRoutes.get(0));
        routeService.deleteRoute(mockRouteTOs.get(0));
    }

    @Test(expected = RouteNotFoundException.class)
    public void testDeleteRoute_Failed() throws Exception {
        when(routeRepository.findOne(mockRouteTOs.get(0).getNumber())).thenReturn(null);
        routeService.deleteRoute(mockRouteTOs.get(0));
    }

    @Test
    public void testGetRoute_Success() throws Exception {
        when(routeRepository.findOne(mockRouteTOs.get(0).getNumber())).thenReturn(mockRoutes.get(0));
        RouteTO routeTO = routeService.getRoute(mockRouteTOs.get(0).getNumber());
        assertEquals(routeTO.getNumber(), mockRouteTOs.get(0).getNumber());
        assertEquals(routeTO.getRouteEntries().size(), mockEntryTOs.size());
        for(int i = 0; i < routeTO.getRouteEntries().size(); i++) {
            RouteEntryTO entryTO = routeTO.getRouteEntries().get(i);
            assertEquals(entryTO.getHour(), mockEntryTOs.get(i).getHour());
            assertEquals(entryTO.getMinute(), mockEntryTOs.get(i).getMinute());
            assertEquals(entryTO.getSeqNumber(), mockEntryTOs.get(i).getSeqNumber());
            assertEquals(entryTO.getDistance(), mockEntryTOs.get(i).getDistance());
            assertEquals(entryTO.getStationName(), mockEntryTOs.get(i).getStationName());
        }
    }

    @Test(expected = RouteNotFoundException.class)
    public void testGetRoute_Failed() throws Exception {
        when(routeRepository.findOne(mockRouteTOs.get(0).getNumber())).thenReturn(null);
        routeService.getRoute(mockRouteTOs.get(0).getNumber());
    }

    @Test
    public void testEditRoute_Success() throws Exception {
        when(routeRepository.findOne(mockRouteTOs.get(0).getNumber())).thenReturn(mockRoutes.get(0));
        routeService.editRoute(mockRouteTOs.get(0));

        ArgumentCaptor<Route> captor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository).save(captor.capture());
        Route route = captor.getValue();

        assertEquals(route.getId(), mockRouteTOs.get(0).getNumber());
        assertEquals(route.getRouteEntries().size() - 1, withChangedRoute(mockRoutes.get(0)).getRouteEntries().size());
    }

    @Test(expected = RouteNotFoundException.class)
    public void testEditRoute_RouteNotFound() throws Exception {
        when(routeRepository.findOne(mockRouteTOs.get(0).getNumber())).thenReturn(null);
        routeService.editRoute(mockRouteTOs.get(0));
    }

    private List<RouteEntry> withList(RouteEntry entry) {
        List<RouteEntry> entries = new ArrayList<RouteEntry>();
        entries.add(entry);
        return entries;
    }

    private Route withChangedRoute(Route route) {
        Route result = new Route();
        result.setId(route.getId());
        result.setRouteEntries(withList(route.getRouteEntries().get(0)));
        return result;
    }
}
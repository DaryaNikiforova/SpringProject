package ru.tsystems.tsproject.sbb2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.tsystems.tsproject.sbb.database.entity.Station;
import ru.tsystems.tsproject.sbb.database.repositories.StationRepository;
import ru.tsystems.tsproject.sbb.services.StationService;
import ru.tsystems.tsproject.sbb.services.exceptions.StationAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
* Implements set of tests for Station service.
* @author Daria Nikiforova
*/

@RunWith(MockitoJUnitRunner.class)
public class StationServiceTest {

    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private StationService stationService = new StationService();

    final private String MOSCOW = "Moscow";
    final private String SAINT_PETERSBURG = "Saint-Petersburg";
    private StationTO defaultStationTO;
    private List<Station> mockStations;

    @Before
    public void setup() {
        defaultStationTO = new StationTO();
        defaultStationTO.setId(0);
        defaultStationTO.setName(MOSCOW);

        mockStations = new ArrayList<Station>();
        Station station = new Station();
        station.setId(0);
        station.setName(MOSCOW);
        mockStations.add(station);

        station = new Station();
        station.setId(1);
        station.setName(SAINT_PETERSBURG);
        mockStations.add(station);
    }

    @Test
    public void testAddStation_Success() throws Exception {
        stationService.addStation(defaultStationTO);

        ArgumentCaptor<Station> captor = ArgumentCaptor.forClass(Station.class);
        verify(stationRepository).save(captor.capture());
        Station station = captor.getValue();
        assertEquals(station.getName(), defaultStationTO.getName());
    }

    @Test(expected = StationAlreadyExistException.class)
    public void testAddStation_Failed() throws Exception {
        when(stationRepository.countByName(anyString())).thenReturn(new Long(1));
        stationService.addStation(defaultStationTO);
    }

    @Test
    public void testGetStations_Success() throws Exception {
        when(stationRepository.findAll()).thenReturn(mockStations);
        List<StationTO> result = stationService.getStations();

        assertEquals(result.size(), mockStations.size());
        for (int i = 0; i < mockStations.size(); i++) {
            assertEquals(result.get(i).getId(), mockStations.get(i).getId());
            assertEquals(result.get(i).getName(), mockStations.get(i).getName());
        }
    }


    @Test(expected = StationNotFoundException.class)
    public void testEditStation_Failed() throws Exception {
        when(stationRepository.countByName(anyString())).thenReturn(new Long(0));
        when(stationRepository.findOne(anyInt())).thenReturn(null);
        stationService.editStation(defaultStationTO);
    }

    @Test
    public void testEditStation_Success() throws Exception {
        StationTO changedStationTO = new StationTO();
        changedStationTO.setId(changedStationTO.getId());
        changedStationTO.setName(SAINT_PETERSBURG);

        when(stationRepository.countByName(SAINT_PETERSBURG)).thenReturn(new Long(0));
        when(stationRepository.findOne(0)).thenReturn(mockStations.get(0));
        stationService.editStation(changedStationTO);

        ArgumentCaptor<Station> captor = ArgumentCaptor.forClass(Station.class);
        verify(stationRepository).save(captor.capture());
        Station station = captor.getValue();
        assertEquals(station.getId(), changedStationTO.getId());
        assertEquals(station.getName(), changedStationTO.getName());
    }

    @Test
    public void testDeleteStation_Success() throws Exception {
        when(stationRepository.findOne(0)).thenReturn(mockStations.get(0));
        stationService.deleteStation(defaultStationTO);
    }

    @Test(expected = StationNotFoundException.class)
    public void testDeleteStation_Failed() throws Exception {
        when(stationRepository.findOne(0)).thenReturn(null);
        stationService.deleteStation(defaultStationTO);
    }

    @Test
    public void testGetStation_Success() throws Exception {
        when(stationRepository.findOne(defaultStationTO.getId())).thenReturn(mockStations.get(0));
        StationTO result = stationService.getStation(defaultStationTO.getId());
        assertEquals(result.getName(), defaultStationTO.getName());
    }

    @Test(expected = StationNotFoundException.class)
    public void testGetStation_Failed() throws Exception {
        when(stationRepository.findOne(defaultStationTO.getId())).thenReturn(null);
        stationService.getStation(defaultStationTO.getId());
    }
}
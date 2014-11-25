//package ru.tsystems.tsproject.sbb2;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import ru.tsystems.tsproject.sbb.database.dao.FactoryDAO;
//import ru.tsystems.tsproject.sbb.database.dao.StationDAO;
//import ru.tsystems.tsproject.sbb.database.entity.Station;
//import ru.tsystems.tsproject.sbb.services.StationService;
//import ru.tsystems.tsproject.sbb.services.exceptions.StationAlreadyExistException;
//import ru.tsystems.tsproject.sbb.transferObjects.StationTO;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//
//import static org.mockito.Mockito.when;
//
///**
// * Implements set of tests for Station service.
// * @author Daria Nikiforova
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class StationServiceTest {
//
//    @Mock
//    private StationDAO stationDAO;
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
//    private StationService stationService = new StationService(null, (StationDAO)null);
//
//    /**
//     * Tests method StationService.addStation. Test passed when expected data equals to created.
//     * @throws Exception
//     */
//    @Test
//    public void testAddStation_Success() throws Exception {
//        String name = "Moscow";
//        StationTO parameterStation = new StationTO();
//        parameterStation.setName(name);
//
//        Station expectedStation = new Station();
//        expectedStation.setName(name);
//
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when(stationDAO.isStationExist(name)).thenReturn(false);
//        when(stationDAO.getStation(name)).thenReturn(expectedStation);
//        stationService.addStation(parameterStation);
//        Station createdStation = stationDAO.getStation(parameterStation.getName());
//        Assert.assertEquals(expectedStation.getName(), createdStation.getName());
//
//    }
//
//    /**
//     * Tests method StationService.addStation. Test passed when throws StationAlreadyExistException.
//     * @throws Exception
//     */
//    @Test(expected = StationAlreadyExistException.class)
//    public void testAddStation_AlreadyExistException() throws Exception {
//        String stationName = "Udelnaya";
//        StationTO parameterStation = new StationTO();
//        parameterStation.setName(stationName);
//
//        Station testedStation = new Station();
//        testedStation.setName(stationName);
//
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        when(stationDAO.isStationExist(stationName)).thenReturn(true);
//        stationService.addStation(parameterStation);
//    }
//}

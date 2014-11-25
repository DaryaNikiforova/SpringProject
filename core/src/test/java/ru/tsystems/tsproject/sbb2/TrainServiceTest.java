//package ru.tsystems.tsproject.sbb2;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//import ru.tsystems.tsproject.sbb.database.dao.FactoryDAO;
//import ru.tsystems.tsproject.sbb.database.dao.RateDAO;
//import ru.tsystems.tsproject.sbb.database.dao.TrainDAO;
//import ru.tsystems.tsproject.sbb.database.entity.Rate;
//import ru.tsystems.tsproject.sbb.database.entity.Train;
//import ru.tsystems.tsproject.sbb.services.TrainService;
//import ru.tsystems.tsproject.sbb.services.exceptions.TrainAlreadyExistException;
//import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//
//import static org.mockito.Mockito.when;
//
///**
// * Implements set of tests for Train service.
// * @author Daria Nikiforova
// */
//@RunWith(MockitoJUnitRunner.class)
//public class TrainServiceTest {
//
//    @Mock
//    private TrainDAO trainDAO;
//
//    @Mock
//    private RateDAO rateDAO;
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
//    private TrainService trainService = new TrainService(null, null, null);
//
//
//    /**
//     * Tests method TrainService.addTrain. Test passed when expected data equals to created.
//     * @throws Exception
//     */
//    @Test
//    public void testAddTrain_Success() throws Exception {
//
//        String name = "Allegro";
//        int number = 111;
//        int seatCount = 100;
//        int rateId = 3;
//
//        TrainTO train = new TrainTO();
//        train.setName(name);
//        train.setNumber(number);
//        train.setSeatCount(seatCount);
//        train.setRateId(rateId);
//
//        Train expectedTrain = new Train();
//        expectedTrain.setName(name);
//        expectedTrain.setId(number);
//        expectedTrain.setSeatCount(seatCount);
//        expectedTrain.setRate(new Rate(rateId));
//
//        when (trainDAO.isTrainExist(train.getNumber())).thenReturn(false);
//        when(trainDAO.getTrain(train.getNumber())).thenReturn(expectedTrain);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        trainService.addTrain(train);
//        Train createdTrain = trainDAO.getTrain(train.getNumber());
//        //verify(trainDAO).addTrain(expectedTrain);
//        Assert.assertEquals(expectedTrain, createdTrain);
//    }
//
//    /**
//     * Tests method TrainService.addTrain. Test passed when throws TrainAlreadyExistException.
//     * @throws Exception
//     */
//    @Test(expected = TrainAlreadyExistException.class)
//    public void testAddTrain_TrainAlreadyExistException() throws Exception {
//        String name = "Allegro";
//        int number = 111;
//        int seatCount = 100;
//        int rateId = 3;
//
//        TrainTO train = new TrainTO();
//        train.setName(name);
//        train.setNumber(number);
//        train.setSeatCount(seatCount);
//        train.setRateId(rateId);
//
//        when (trainDAO.isTrainExist(train.getNumber())).thenReturn(true);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//        trainService.addTrain(train);
//    }
//}

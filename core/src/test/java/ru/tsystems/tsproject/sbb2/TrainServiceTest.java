package ru.tsystems.tsproject.sbb2;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.tsystems.tsproject.sbb.database.entity.Rate;
import ru.tsystems.tsproject.sbb.database.entity.Train;
import ru.tsystems.tsproject.sbb.database.repositories.RateRepository;
import ru.tsystems.tsproject.sbb.database.repositories.TrainRepository;
import ru.tsystems.tsproject.sbb.services.TrainService;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.RateTO;
import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
/**
 * Implements set of tests for Train service.
 * @author Daria Nikiforova
 */
@RunWith(MockitoJUnitRunner.class)
public class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;
    @Mock
    private RateRepository rateRepository;
    @InjectMocks
    private TrainService trainService = new TrainService();

    Rate mockRate;
    Train mockTrain;
    TrainTO mockTrainTO;

    @Before
    public void setup() {
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

        mockTrainTO = new TrainTO();
        mockTrainTO.setName(mockTrain.getName());
        mockTrainTO.setNumber(mockTrain.getId());
        mockTrainTO.setRateId(mockTrain.getRate().getId());
        mockTrainTO.setRateName(mockTrain.getRate().getName());
        mockTrainTO.setSeatCount(mockTrain.getSeatCount());
    }

    @Test
    public void testAddTrain_Success() throws Exception {
        when(trainRepository.exists(mockTrainTO.getNumber())).thenReturn(false);
        trainService.addTrain(mockTrainTO);

        ArgumentCaptor<Train> captor = ArgumentCaptor.forClass(Train.class);
        verify(trainRepository).save(captor.capture());
        Train train = captor.getValue();
        assertEquals(train.getId(), mockTrainTO.getNumber());
        assertEquals(train.getName(), mockTrainTO.getName());
        assertEquals(train.getRate().getId(), mockTrainTO.getRateId());
        assertEquals(train.getSeatCount(), mockTrainTO.getSeatCount());
    }

    @Test(expected = TrainAlreadyExistException.class)
    public void testAddTrain_Failed() throws Exception {
        when(trainRepository.exists(mockTrainTO.getNumber())).thenReturn(true);
        trainService.addTrain(mockTrainTO);
    }

    @Test
    public void testGetTrainRates_Success() throws Exception {
        when(rateRepository.findByForTrainTrue()).thenReturn(withList(mockRate));
        List<RateTO> rateTOs = trainService.getTrainRates();
        assertEquals(rateTOs.size(), 1);
        assertEquals(rateTOs.get(0).getId(), mockRate.getId());
        assertEquals(rateTOs.get(0).getName(), mockRate.getName());
    }

    @Test
    public void testGetTrains_Success() throws Exception {
        when(trainRepository.findAll()).thenReturn(withList(mockTrain));
        List<TrainTO> trainTOs = trainService.getTrains();
        assertEquals(trainTOs.size(), 1);
        assertEquals(trainTOs.get(0).getNumber(), mockTrain.getId());
        assertEquals(trainTOs.get(0).getName(), mockTrain.getName());
        assertEquals(trainTOs.get(0).getSeatCount(), mockTrain.getSeatCount());
        assertEquals(trainTOs.get(0).getRateId(), mockTrain.getRate().getId());
        assertEquals(trainTOs.get(0).getRateName(), mockTrain.getRate().getName());
    }

    @Test
    public void testGetTrain_Success() throws Exception {
        when(trainRepository.findOne(mockTrain.getId())).thenReturn(mockTrain);
        TrainTO trainTO = trainService.getTrain(mockTrain.getId());
        assertEquals(trainTO.getNumber(), mockTrain.getId());
        assertEquals(trainTO.getName(), mockTrain.getName());
        assertEquals(trainTO.getSeatCount(), mockTrain.getSeatCount());
        assertEquals(trainTO.getRateId(), mockTrain.getRate().getId());
        assertEquals(trainTO.getRateName(), mockTrain.getRate().getName());
    }

    @Test(expected = TrainNotFoundException.class)
    public void testGetTrain_Failed() throws Exception {
        when(trainRepository.findOne(mockTrain.getId())).thenReturn(null);
        trainService.getTrain(mockTrain.getId());
    }

    @Test
    public void testDeleteTrain_Success() throws Exception {
        when(trainRepository.findOne(mockTrainTO.getNumber())).thenReturn(mockTrain);
        trainService.deleteTrain(mockTrainTO);

        ArgumentCaptor<Train> captor = ArgumentCaptor.forClass(Train.class);
        verify(trainRepository).delete(captor.capture());
        Train train = captor.getValue();
        assertEquals(train.getId(), mockTrainTO.getNumber());
        assertEquals(train.getName(), mockTrainTO.getName());
        assertEquals(train.getRate().getId(), mockTrainTO.getRateId());
        assertEquals(train.getSeatCount(), mockTrainTO.getSeatCount());
    }

    @Test(expected = TrainNotFoundException.class)
    public void testDeleteTrain_Failed() throws Exception {
        when(trainRepository.findOne(mockTrainTO.getNumber())).thenReturn(null);
        trainService.deleteTrain(mockTrainTO);
    }

    @Test
    public void testEditTrain_Success() throws Exception {
        when(trainRepository.findOne(mockTrainTO.getNumber())).thenReturn(mockTrain);
        trainService.editTrain(mockTrainTO);
        ArgumentCaptor<Train> captor = ArgumentCaptor.forClass(Train.class);
        verify(trainRepository).save(captor.capture());
        Train train = captor.getValue();
        assertEquals(train.getId(), mockTrainTO.getNumber());
        assertEquals(train.getName(), mockTrainTO.getName());
        assertEquals(train.getRate().getId(), mockTrainTO.getRateId());
        assertEquals(train.getSeatCount(), mockTrainTO.getSeatCount());
    }

    @Test(expected = TrainNotFoundException.class)
    public void testEditTrain_Failed() throws Exception {
        when(trainRepository.findOne(mockTrainTO.getNumber())).thenReturn(null);
        trainService.editTrain(mockTrainTO);
    }

    private <T> List<T> withList(T item) {
        List<T> list = new ArrayList<T>();
        list.add(item);
        return list;
    }
}
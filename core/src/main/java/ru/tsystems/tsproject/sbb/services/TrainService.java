package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.Rate;
import ru.tsystems.tsproject.sbb.database.entity.Train;
import ru.tsystems.tsproject.sbb.database.repositories.RateRepository;
import ru.tsystems.tsproject.sbb.database.repositories.TrainRepository;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.TrainNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.RateTO;
import ru.tsystems.tsproject.sbb.transferObjects.TrainTO;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods interacting with DAO methods. Needs to get and change data in database.
 * @author Daria Nikiforova
 */

@Service
@Transactional
@PreAuthorize("denyAll")
public class TrainService {
    private static final Logger LOGGER = Logger.getLogger(TrainService.class);

    @Inject TrainRepository trainRepository;
    @Inject RateRepository rateRepository;

    /**
     * Returns all rates that specified for trains (express, speedy, etc.). Throws exception
     * if database connection is lost, bad request or error with transaction.
     * @return
     * @throws ServiceException
     */

    @PreAuthorize("hasRole('admin')")
    public List<RateTO> getTrainRates() throws ServiceException {
        List<Rate> rates = new ArrayList<Rate>();
        LOGGER.info("Получение типов поездов");
        try {
            rates = rateRepository.findByForTrainTrue();
        } catch (PersistenceException ex) {
            LOGGER.error("Ошибка при получении информации о маршрутах");
            throw new ServiceException("Ошибка при получении информации о маршрутах");
        }
        List<RateTO> result = new ArrayList<RateTO>();
            for (Rate rate : rates) {
                RateTO rateTO = new RateTO();
                rateTO.setId(rate.getId());
                rateTO.setName(rate.getName());
                result.add(rateTO);
            }
        return result;
    }

    /**
     * Adds train to database. Throws exception if database connection is lost,
     * bad request or error with transaction. Throws TrainAlreadyExistException if trying
     * to add train that already exists into database.
     * @param trainTO train that want to add
     * @throws ServiceException
     */

    @PreAuthorize("hasRole('admin')")
    public void addTrain(TrainTO trainTO) throws TrainAlreadyExistException
    {
        //try {
            if (!trainRepository.exists(trainTO.getNumber())) {
                Train train = new Train(trainTO.getNumber(), trainTO.getSeatCount(), trainTO.getName(), new Rate(trainTO.getRateId()));
                trainRepository.save(train);
                LOGGER.info("Поезд добавлен в базу данных");
            }
            else {
                LOGGER.error("Поезд с таким номером уже существует в базе данных");
                throw new TrainAlreadyExistException("Поезд с таким номером уже существует в базе данных");
            }
        //} //catch (PersistenceException e) {
            //LOGGER.error("Невозможно добавить поезд в базу данных");
            //throw new ServiceException("Невозможно добавить поезд в базу данных");
        //}
    }
    /**
     * Returns list of all trains. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @return list of trains
     * @throws ServiceException
     */

    @PreAuthorize("hasRole('admin')")
    public List<TrainTO> getTrains() throws ServiceException {
        List<Train> list = new ArrayList<Train>();
        LOGGER.info("Получение списка поездов");
        try {
            list = trainRepository.findAll();
        } catch(PersistenceException ex) {
            LOGGER.error("Ошибка при получении информации о поездах");
            throw new ServiceException("Ошибка о получении информации о поездах");
        }
        List<TrainTO> result = new ArrayList<TrainTO>();
        for (Train t:list) {
            TrainTO train = new TrainTO();
            train.setNumber(t.getId());
            train.setName(t.getName());
            train.setSeatCount(t.getSeatCount());
            train.setRateName(t.getRate().getName());
            train.setRateId(t.getRate().getId());
            result.add(train);
        }
        return result;
    }

    public TrainTO getTrain(int id) throws TrainNotFoundException {
        Train train = trainRepository.findOne(id);
        TrainTO trainTO;
        if (train != null) {
            trainTO = new TrainTO();
            trainTO.setName(train.getName());
            trainTO.setNumber(train.getId());
            trainTO.setSeatCount(train.getSeatCount());
            trainTO.setRateName(train.getRate().getName());
            trainTO.setRateId(train.getRate().getId());
        } else {
            throw new TrainNotFoundException("Поезд с номером " + id + "не найден в базе данных");
        }
        return trainTO;
    }

    @PreAuthorize("hasRole('admin')")
    public void editTrain(TrainTO trainTO) throws TrainAlreadyExistException {
        if (trainRepository.countById(trainTO.getNumber()) == 0) {
            Train train = new Train(trainTO.getNumber(), trainTO.getSeatCount(), trainTO.getName(), new Rate(trainTO.getRateId()));
            trainRepository.save(train);
            LOGGER.info("Редактирование станции " + trainTO.getName() + " в базе данных");
        } else {
            LOGGER.error("Поезд с номером " + trainTO.getNumber() + "уже содержится в базе данных");
            throw new TrainAlreadyExistException("Поезд с номером " + trainTO.getNumber() + "уже содержится в базе данных");
        }
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteTrain(TrainTO trainTO) throws Exception {
        if (trainRepository.findOne(trainTO.getNumber()) == null) {
            throw new Exception();
        }
        trainRepository.delete(trainTO.getNumber());
        LOGGER.info("Удаление станции " + trainTO.getName() + " из базы данных");
    }

}

package ru.tsystems.tsproject.sbb.services;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsystems.tsproject.sbb.database.entity.Station;
import ru.tsystems.tsproject.sbb.database.repositories.StationRepository;
import ru.tsystems.tsproject.sbb.services.exceptions.ServiceException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationAlreadyExistException;
import ru.tsystems.tsproject.sbb.services.exceptions.StationNotFoundException;
import ru.tsystems.tsproject.sbb.transferObjects.StationTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods interacting with DAO methods. Needs to get and change data in database.
 * @author Daria Nikiforova
 */

@Service
@Transactional
@PreAuthorize("denyAll")
public class StationService {
    private static final Logger LOGGER = Logger.getLogger(Station.class);

    @Inject StationRepository stationRepository;

    /**
     * Adds station to database. Throws PersistenceException if database connection is lost,
     * bad request or error with transaction. Throws StationAlreadyExistsException when the same
     * record already exists in database.
     * @param stationTO added to database
     * @throws ServiceException
     */
    @PreAuthorize("hasRole('addRole')")
    public void addStation(StationTO stationTO) throws StationAlreadyExistException {
            if (stationRepository.countByName(stationTO.getName()) == 0) {
                Station station = new Station();
                station.setName(stationTO.getName());
                stationRepository.save(station);
                LOGGER.info("Добавление станции в базу данных");
            } else {
                LOGGER.error("Станция уже существует в базе данных");
                throw new StationAlreadyExistException("Станция уже существует в базе данных");
            }
    }

    /**
     * Return list of all stations stored in database. Throws exception if database connection is lost,
     * bad request or error with transaction.
     * @return list of stations.
     * @throws ServiceException
     */
    @PreAuthorize("hasRole('admin')")
    public List<StationTO> getStations() {
        List<Station> stations = stationRepository.findAll();
        List<StationTO> stationTOs = new ArrayList<StationTO>();
        for (Station station : stations) {
            stationTOs.add(new StationTO(station.getId(), station.getName()));
        }
        return  stationTOs;
    }

    @PreAuthorize("hasRole('admin')")
    public void editStation(StationTO stationTO) throws StationNotFoundException {
        Station station = stationRepository.findOne(stationTO.getId());
        if (station == null) {
            throw new StationNotFoundException("");
        } else {
            station.setName(stationTO.getName());
            stationRepository.save(station);
            LOGGER.info("Редактирование станции " + stationTO.getName() + " в базе данных");
        }
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteStation(StationTO stationTO) throws StationNotFoundException {
        Station station = stationRepository.findOne(stationTO.getId());
        if (station == null) {
            throw new StationNotFoundException("Станции с id=" + stationTO.getId() + " не существует");
        }
        stationRepository.delete(station);
        LOGGER.info("Удаление станции " + stationTO.getName() + " из базы данных");
    }

    @PreAuthorize("hasRole('admin')")
    public StationTO getStation(int id) throws StationNotFoundException {
        Station station = stationRepository.findOne(id);
        StationTO stationTO;
        if (station != null) {
            stationTO = new StationTO();
            stationTO.setName(station.getName());
            stationTO.setId(station.getId());
        } else {
            LOGGER.error("Станция с id=" + id + " не содержится в базе данных");
            throw new StationNotFoundException("Станция с id=" + id + " не найдена");
        }
        return stationTO;
    }
}

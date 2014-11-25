package ru.tsystems.tsproject.sbb.database.dao;

import ru.tsystems.tsproject.sbb.database.entity.Train;

import java.util.List;

/**
 * Created by apple on 14.10.14.
 */
public interface TrainDAO {
    List<Train> getAllTrains();
    void addTrain(Train train);
    void deleteTrain(Train train);
    void updateTrain(Train train);
    List<Train> getTrains();
    boolean isTrainExist(int id);
    Train getTrain(int id);
}

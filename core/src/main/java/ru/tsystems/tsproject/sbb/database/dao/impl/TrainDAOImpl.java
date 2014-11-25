package ru.tsystems.tsproject.sbb.database.dao.impl;

import org.springframework.stereotype.Repository;
import ru.tsystems.tsproject.sbb.database.dao.TrainDAO;
import ru.tsystems.tsproject.sbb.database.entity.Train;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by apple on 04.10.14.
 */

@Repository
public final class TrainDAOImpl implements TrainDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addTrain(Train train) {
        entityManager.persist(train);
    }

    @Override
    public void deleteTrain(Train train) {
        entityManager.createQuery("delete from Train t where t.id = :number")
                .setParameter("number", train.getId())
                .executeUpdate();
    }

    /*@Override
    public List<Train> getTrains(String aStation, String bStation, Timestamp timeFrom, Timestamp timeTo) {
        return (List<Train>) entityManager.createQuery("select trL.train from TimetableRecord trL join trL trR " +
                "where trR.station.name = :bStation " +
                "and trL.station.name = :aStation and trL.time >= :timeFrom and trL.time <= :timeTo")
                .setParameter("aStation", aStation)
                .setParameter("bStation", bStation)
                .setParameter("timeFrom", timeFrom)
                .setParameter("timeTo", timeTo);
    }*/

    @Override
    public List<Train> getAllTrains() {
        return entityManager.createQuery("select t from Train t", Train.class).getResultList();
    }


    @Override
    public void updateTrain(Train train) {
        entityManager.createQuery("update Train t set t=:train where t.id=:number")
                     .setParameter("train", train)
                     .setParameter("number", train.getId())
                     .executeUpdate();
    }

    @Override
    public List<Train> getTrains() {
        return entityManager.createQuery("select t from Train t").getResultList();
    }

    @Override
    public boolean isTrainExist(int id) {
        return !entityManager.createQuery("select t from Train t where t.id = :id")
                             .setParameter("id", id)
                             .getResultList()
                             .isEmpty();
    }

    @Override
    public Train getTrain(int id) {
        return (Train) entityManager.createQuery("select t from Train t where t.id = :id").getSingleResult();
    }

}
